package dev.vozniack.soodoku.core.api.controller

import dev.vozniack.soodoku.core.api.dto.GameDto
import dev.vozniack.soodoku.core.api.dto.GameHistoryDto
import dev.vozniack.soodoku.core.api.dto.NewGameRequestDto
import dev.vozniack.soodoku.core.api.dto.MoveRequestDto
import dev.vozniack.soodoku.core.api.dto.NoteRequestDto
import dev.vozniack.soodoku.core.domain.types.Difficulty
import dev.vozniack.soodoku.core.service.GameService
import dev.vozniack.soodoku.core.service.GameHistoryService
import java.util.UUID
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/games")
class GameController(private val gameService: GameService, private val gameHistoryService: GameHistoryService) {

    @GetMapping("/{id}")
    fun get(@PathVariable id: UUID): GameDto = gameService.get(id)

    @GetMapping("/ongoing")
    fun getOngoing(pageable: Pageable): Slice<GameDto> = gameService.getOngoing(pageable)

    @GetMapping("/history")
    fun getHistory(
        @RequestParam(required = false) difficulty: Difficulty?,
        @RequestParam(required = false) victory: Boolean?,
        pageable: Pageable,
    ): Slice<GameHistoryDto> = gameHistoryService.get(difficulty, victory, pageable)

    @PostMapping
    fun new(@RequestBody newGameRequestDto: NewGameRequestDto): GameDto = gameService.new(newGameRequestDto)

    @PutMapping("/{id}/pause")
    fun pause(@PathVariable id: UUID): GameDto = gameService.pause(id)

    @PutMapping("/{id}/resume")
    fun resume(@PathVariable id: UUID): GameDto = gameService.resume(id)

    @PutMapping("/{id}/move")
    fun move(@PathVariable id: UUID, @RequestBody move: MoveRequestDto): GameDto = gameService.move(id, move)

    @PutMapping("/{id}/revert")
    fun revert(@PathVariable id: UUID): GameDto = gameService.revert(id)

    @PutMapping("/{id}/note")
    fun note(@PathVariable id: UUID, @RequestBody request: NoteRequestDto): GameDto = gameService.note(id, request)

    @DeleteMapping("/{id}/note")
    fun wipeNotes(@PathVariable id: UUID) = gameService.deleteNotes(id)

    @PutMapping("/{id}/hint")
    fun hint(@PathVariable id: UUID): GameDto = gameService.hint(id)

    @PutMapping("/{id}/end")
    fun end(@PathVariable id: UUID): GameDto = gameService.end(id)

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID) {
        gameService.delete(id)
    }
}
