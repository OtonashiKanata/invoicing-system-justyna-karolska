package pl.futurecollars.invoicing.service

import pl.futurecollars.invoicing.db.file.FilesService
import pl.futurecollars.invoicing.service.IdService
import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Path

class IdServiceIntegrationTest extends Specification {

    private Path nextIdDbPath = File.createTempFile('nextId', '.txt').toPath()

    def "next id starts from 1 if file was empty"() {
        given:
        IdService idService = new IdService(nextIdDbPath, new FilesService())

        expect:
        ['1'] == Files.readAllLines(nextIdDbPath)

        and:
        1L == idService.setId()
        ['2'] == Files.readAllLines(nextIdDbPath)

        and:
        2L == idService.setId()
        ['3'] == Files.readAllLines(nextIdDbPath)

        and:
        3L == idService.setId()
        ['4'] == Files.readAllLines(nextIdDbPath)
    }

    def "if file was not empty, next id starts from last number"() {
        given:
        Files.writeString(nextIdDbPath, "666")
        IdService idService = new IdService(nextIdDbPath, new FilesService())

        expect:
        ['666'] == Files.readAllLines(nextIdDbPath)

        and:
        idService.setId()
        ['667'] == Files.readAllLines(nextIdDbPath)

        and:
        idService.setId()
        ['668'] == Files.readAllLines(nextIdDbPath)

        and:
        idService.setId()
        ['669'] == Files.readAllLines(nextIdDbPath)

    }

    def "empty file returns empty collection"() {
        expect:
        [] == Files.readAllLines(nextIdDbPath)
    }

}