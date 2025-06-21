package dev.vozniack.soodoku.core.api.controller

import dev.vozniack.soodoku.core.api.dto.GameDto
import dev.vozniack.soodoku.core.api.dto.NewGameRequestDto
import dev.vozniack.soodoku.core.api.dto.MoveRequestDto
import dev.vozniack.soodoku.core.api.dto.NoteRequestDto
import dev.vozniack.soodoku.core.service.GameService
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
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/games")
class GameController(private val gameService: GameService) {

    @GetMapping("/{id}")
    fun get(@PathVariable id: UUID): GameDto = gameService.get(id)

    @GetMapping
    fun get(pageable: Pageable): Slice<GameDto> = gameService.get(pageable)

    @GetMapping("/last")
    fun getLast(): GameDto? = gameService.getLast()

    @PostMapping
    fun new(@RequestBody newGameRequestDto: NewGameRequestDto): GameDto = gameService.new(newGameRequestDto)

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
