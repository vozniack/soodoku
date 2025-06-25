package dev.vozniack.soodoku.core.service

import dev.vozniack.soodoku.core.api.dto.GameDto
import dev.vozniack.soodoku.core.api.dto.NewGameRequestDto
import dev.vozniack.soodoku.core.api.dto.MoveRequestDto
import dev.vozniack.soodoku.core.api.dto.NoteRequestDto
import dev.vozniack.soodoku.core.api.mapper.toDtoWithStatus
import dev.vozniack.soodoku.core.domain.entity.Game
import dev.vozniack.soodoku.core.domain.entity.GameMove
import dev.vozniack.soodoku.core.domain.entity.GameSession
import dev.vozniack.soodoku.core.domain.entity.User
import dev.vozniack.soodoku.core.domain.extension.end
import dev.vozniack.soodoku.core.domain.extension.isEnd
import dev.vozniack.soodoku.core.domain.extension.parseNotes
import dev.vozniack.soodoku.core.domain.extension.serializeNotes
import dev.vozniack.soodoku.core.domain.extension.toGame
import dev.vozniack.soodoku.core.domain.repository.GameRepository
import dev.vozniack.soodoku.core.domain.extension.toSoodoku
import dev.vozniack.soodoku.core.domain.types.MoveType
import dev.vozniack.soodoku.core.internal.exception.ConflictException
import dev.vozniack.soodoku.core.internal.exception.NotFoundException
import dev.vozniack.soodoku.core.internal.exception.UnauthorizedException
import dev.vozniack.soodoku.core.internal.logging.KLogging
import dev.vozniack.soodoku.lib.Soodoku
import dev.vozniack.soodoku.lib.exception.SoodokuMappingException
import dev.vozniack.soodoku.lib.extension.flatBoard
import dev.vozniack.soodoku.lib.extension.move
import dev.vozniack.soodoku.lib.extension.status
import dev.vozniack.soodoku.lib.extension.value
import java.time.LocalDateTime
import java.util.UUID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GameService(
    private val gameRepository: GameRepository,
    private val gameHistoryService: GameHistoryService,
    private val userService: UserService,
    private val coroutineScope: CoroutineScope
) {

    fun get(id: UUID): GameDto {
        val game: Game = getGame(id = id, allowPaused = true, allowFinished = true)

        return game toDtoWithStatus game.toSoodoku().status()
    }

    fun getOngoing(pageable: Pageable): Slice<GameDto> {
        val user: User = userService.currentlyLoggedUser()
            ?: throw UnauthorizedException("You don't have access to this resource")

        return gameRepository.findOngoingGames(user.id, pageable).map {
            it.toDtoWithStatus(it.toSoodoku().status())
        }
    }

    fun new(newGameRequestDto: NewGameRequestDto): GameDto {
        val user: User? = userService.currentlyLoggedUser()

        val soodoku = Soodoku(Soodoku.Difficulty.valueOf(newGameRequestDto.difficulty.name))
        val status: Soodoku.Status = soodoku.status()

        val game: Game = gameRepository.save(
            soodoku.toGame(user = user, difficulty = newGameRequestDto.difficulty, hints = 3)
        )

        gameRepository.save(game.also { it.sessions.add(GameSession(game = it, startedAt = it.startedAt)) })

        return game toDtoWithStatus status
    }

    fun pause(id: UUID): GameDto {
        val game: Game = getGame(id = id)
        val now = LocalDateTime.now()

        game.sessions.filter { it.pausedAt == null }.maxByOrNull { it.startedAt }?.let { it.pausedAt = now }
            ?: throw ConflictException("Can't pause game $id because it's already paused")

        return gameRepository.save(game.apply { updatedAt = now }) toDtoWithStatus game.toSoodoku().status()
    }

    fun resume(id: UUID): GameDto {
        val game: Game = getGame(id = id, allowPaused = true)
        val now = LocalDateTime.now()

        if (game.sessions.any { it.pausedAt == null }) {
            throw ConflictException("Can't resume game $id because it's not paused")
        }

        game.sessions.add(GameSession(game = game, startedAt = now))

        return gameRepository.save(game.apply { updatedAt = now }) toDtoWithStatus game.toSoodoku().status()
    }

    fun move(id: UUID, move: MoveRequestDto): GameDto {
        var game: Game = getGame(id = id)

        val soodoku = game.toSoodoku()

        val valueBefore: Int = try {
            soodoku.value(move.row, move.col).also {
                soodoku.move(move.row, move.col, move.value)
            }
        } catch (exception: SoodokuMappingException) {
            throw ConflictException("This move is incorrect")
        }

        val status: Soodoku.Status = soodoku.status()

        game.apply {
            currentBoard = status.board.flatBoard()
            updatedAt = LocalDateTime.now()
        }.also {
            it.moves.add(GameMove(game = it, row = move.row, col = move.col, before = valueBefore, after = move.value))
        }

        game = gameRepository.save(game checkEndGameWith status)
        return game toDtoWithStatus game.toSoodoku().status()
    }

    fun revert(id: UUID): GameDto {
        val game: Game = getGame(id = id)

        if (game.moves.isEmpty()) {
            throw ConflictException("Nothing to revert")
        }

        val soodoku = game.toSoodoku()
        val lastMove = game.moves.last { it.type == MoveType.NORMAL && it.revertedAt == null }

        lastMove.let {
            soodoku.move(it.row, it.col, it.before)
        }

        val status: Soodoku.Status = soodoku.status()

        game.apply {
            currentBoard = status.board.flatBoard()
            updatedAt = LocalDateTime.now()
        }.also {
            it.moves.find { move -> move.id === lastMove.id }?.apply {
                revertedAt = LocalDateTime.now()
            }

            it.moves.add(
                GameMove(
                    game = it,
                    type = MoveType.REVERT,
                    row = lastMove.row,
                    col = lastMove.col,
                    before = lastMove.after,
                    after = lastMove.before
                )
            )
        }

        return gameRepository.save(game) toDtoWithStatus status
    }

    fun note(id: UUID, request: NoteRequestDto): GameDto {
        val game: Game = getGame(id = id)

        val parsedNotes = game.parseNotes()
        val key = request.row to request.col

        if (request.values.isEmpty()) {
            parsedNotes.remove(key)
        } else {
            parsedNotes[key] = request.values.toList()
        }

        game.apply {
            notes = parsedNotes.serializeNotes()
            updatedAt = LocalDateTime.now()
        }

        return gameRepository.save(game) toDtoWithStatus game.toSoodoku().status()
    }

    fun deleteNotes(id: UUID): GameDto {
        val game: Game = getGame(id)

        game.apply {
            notes = null
            updatedAt = LocalDateTime.now()
        }

        return gameRepository.save(game) toDtoWithStatus game.toSoodoku().status()
    }

    fun hint(id: UUID): GameDto {
        var game: Game = getGame(id = id)

        if (game.hints < 1) {
            throw ConflictException("You don't have more hints for game $id!")
        }

        val soodoku = game.toSoodoku()
        var status: Soodoku.Status = soodoku.status()

        val allUnlockedCells = (0..8).flatMap { row -> (0..8).map { col -> row to col } }
            .filter { (row, col) -> (row to col) !in status.locks }

        val (row, col) = allUnlockedCells.filter { (row, col) -> status.board[row][col] == 0 }
            .takeIf { it.isNotEmpty() }?.random()
            ?: allUnlockedCells.filter { (row, col) ->
                status.board[row][col] != status.solved[row][col]
            }.randomOrNull()
            ?: throw ConflictException("No available cells for game $id")

        val hint = status.solved[row][col]

        val valueBefore: Int = soodoku.value(row, col).also {
            soodoku.move(row, col, hint)
        }

        val parsedNotes = game.parseNotes()
        parsedNotes.remove(row to col)

        status = soodoku.status()

        game.apply {
            currentBoard = status.board.flatBoard()
            notes = parsedNotes.serializeNotes()
            hints -= 1
            updatedAt = LocalDateTime.now()
        }.also {
            it.moves.add(
                GameMove(
                    game = it,
                    type = MoveType.HINT,
                    row = row,
                    col = col,
                    before = valueBefore,
                    after = hint
                )
            )
        }

        game = gameRepository.save(game checkEndGameWith status)
        return game toDtoWithStatus game.toSoodoku().status()
    }

    fun end(id: UUID): GameDto {
        var game: Game = getGame(id = id, allowPaused = true)

        game = gameRepository.save(game.end())
        saveHistory(game)

        return game toDtoWithStatus game.toSoodoku().status()
    }

    @Transactional
    fun delete(id: UUID) {
        gameHistoryService.delete(id)
        gameRepository.delete(getGame(id, true))
    }

    private fun getGame(id: UUID, allowPaused: Boolean = false, allowFinished: Boolean = false): Game =
        gameRepository.findById(id).takeIf { it.isPresent }?.get()?.let { game ->
            game.takeIf { it.user != null }?.let {
                if (it.user != userService.currentlyLoggedUser()) {
                    throw UnauthorizedException("You don't have access to this game")
                }
            }

            if (!allowPaused && !(game.sessions.any { it.pausedAt == null })) {
                throw ConflictException("Game $id is paused")
            }

            if (!allowFinished && game.finishedAt != null) {
                throw ConflictException("Game $id is already finished!")
            }

            game
        } ?: throw NotFoundException("Not found game with id $id")

    private infix fun Game.checkEndGameWith(status: Soodoku.Status): Game =
        isEnd(status).takeIf { it }?.let { end(); saveHistory(this); this } ?: this

    private fun saveHistory(game: Game) = coroutineScope.launch {
        runCatching { gameHistoryService.save(game) }.onFailure {
            logger.error { "Failed to store game history ${game.id}: ${it.message}" }
        }
    }

    companion object : KLogging()
}
