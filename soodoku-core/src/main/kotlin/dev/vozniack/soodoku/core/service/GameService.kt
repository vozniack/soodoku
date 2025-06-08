package dev.vozniack.soodoku.core.service

import dev.vozniack.soodoku.core.api.dto.GameDto
import dev.vozniack.soodoku.core.api.dto.MoveDto
import dev.vozniack.soodoku.core.api.dto.NewGameDto
import dev.vozniack.soodoku.core.api.mapper.toDtoWithStatus
import dev.vozniack.soodoku.core.domain.entity.Game
import dev.vozniack.soodoku.core.domain.entity.Move
import dev.vozniack.soodoku.core.domain.entity.User
import dev.vozniack.soodoku.core.domain.repository.GameRepository
import dev.vozniack.soodoku.core.extension.toSoodoku
import dev.vozniack.soodoku.core.internal.exception.ConflictException
import dev.vozniack.soodoku.core.internal.exception.NotFoundException
import dev.vozniack.soodoku.core.internal.exception.UnauthorizedException
import dev.vozniack.soodoku.lib.Soodoku
import dev.vozniack.soodoku.lib.extension.flatBoard
import dev.vozniack.soodoku.lib.extension.flatLocks
import dev.vozniack.soodoku.lib.extension.move
import dev.vozniack.soodoku.lib.extension.status
import java.time.LocalDateTime
import java.util.UUID
import org.springframework.stereotype.Service

@Service
class GameService(
    private val gameRepository: GameRepository,
    private val userService: UserService
) {

    fun get(id: UUID): GameDto {
        val game: Game = getGame(id)

        return game toDtoWithStatus game.toSoodoku().status()
    }

    fun new(newGameDto: NewGameDto): GameDto {
        val user: User? = userService.currentlyLoggedUser()

        val soodoku = Soodoku(Soodoku.Difficulty.valueOf(newGameDto.difficulty.name))
        val status: Soodoku.Status = soodoku.status()

        return gameRepository.save(
            Game(
                initialBoard = status.board.flatBoard(),
                currentBoard = status.board.flatBoard(),
                locks = status.locks.flatLocks(),
                difficulty = newGameDto.difficulty,
                user = user
            )
        ) toDtoWithStatus status
    }

    fun move(id: UUID, move: MoveDto): GameDto {
        val game: Game = getGame(id)

        if (game.finishedAt != null) {
            throw ConflictException("Game $id is already finished!")
        }

        val soodoku = game.toSoodoku().also {
            it.move(move.row, move.col, move.value)
        }

        val status: Soodoku.Status = soodoku.status()

        game.apply {
            currentBoard = status.board.flatBoard()
            updatedAt = LocalDateTime.now()
        }.also {
            it.moves.add(Move(game = it, row = move.row, col = move.col, before = 0, after = move.value))
        }

        return gameRepository.save(game) toDtoWithStatus status
    }

    fun undo(id: UUID): GameDto {
        val game: Game = getGame(id)

        if (game.finishedAt != null) {
            throw ConflictException("Game $id is already finished!")
        }

        if (game.moves.isEmpty()) {
            throw ConflictException("Nothing to undo")
        }

        val soodoku = game.toSoodoku()

        game.moves.last().let {
            soodoku.move(it.row, it.col, it.before)
        }

        val status: Soodoku.Status = soodoku.status()

        game.apply {
            currentBoard = status.board.flatBoard()
            updatedAt = LocalDateTime.now()
        }.run { moves.removeLast() }

        return gameRepository.save(game) toDtoWithStatus status
    }

    fun end(id: UUID): GameDto {
        val game: Game = getGame(id)

        game.apply {
            updatedAt = LocalDateTime.now()
            finishedAt = LocalDateTime.now()
        }

        return game toDtoWithStatus game.toSoodoku().status()
    }

    fun delete(id: UUID) {
        gameRepository.delete(getGame(id))
    }

    private fun getGame(id: UUID): Game = gameRepository.findById(id).takeIf { it.isPresent }?.get()?.let { game ->
        game.takeIf { it.user != null }?.let {
            if (it.user != userService.currentlyLoggedUser()) {
                throw UnauthorizedException("You don't have access to this game")
            }
        }

        game
    } ?: throw NotFoundException("Not found game with id $id")
}
