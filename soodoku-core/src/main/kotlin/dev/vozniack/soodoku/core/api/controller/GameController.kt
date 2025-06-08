package dev.vozniack.soodoku.core.api.controller

import dev.vozniack.soodoku.core.api.dto.GameDto
import dev.vozniack.soodoku.core.api.dto.MoveDto
import dev.vozniack.soodoku.core.api.dto.NewGameDto
import dev.vozniack.soodoku.core.service.GameService
import java.util.UUID
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

    @PostMapping
    fun new(@RequestBody newGameDto: NewGameDto): GameDto = gameService.new(newGameDto)

    @PutMapping("/{id}/move")
    fun move(@PathVariable id: UUID, @RequestBody move: MoveDto): GameDto = gameService.move(id, move)

    @PutMapping("/{id}/undo")
    fun undo(@PathVariable id: UUID): GameDto = gameService.undo(id)

    @PutMapping("/{id}/end")
    fun end(@PathVariable id: UUID): GameDto = gameService.end(id)

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID) {
        gameService.delete(id)
    }
}
