package pl.futurecollars.invoicing.service

import pl.futurecollars.invoicing.db.file.FilesService
import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Path

class IdServiceTest extends Specification {

    private Path nextIdDbPath = File.createTempFile('nextId', '.txt').toPath()
    private Path nextIdDbPathWrong = File.createTempFile('nnn', '.txt').toPath()

    def "next id starts from 1 if file was empty"() {
        given:
        IdService idService = new IdService(nextIdDbPath, new FilesService())

        expect:
        ['1'] == Files.readAllLines(nextIdDbPath)

        and:
        1L == idService.getNextIdAndIncrement()
        ['2'] == Files.readAllLines(nextIdDbPath)

        and:
        2L == idService.getNextIdAndIncrement()
        ['3'] == Files.readAllLines(nextIdDbPath)

        and:
        3L == idService.getNextIdAndIncrement()
        ['4'] == Files.readAllLines(nextIdDbPath)

    }

    def "if file was not empty, next id starts from last number"() {
        given:
        Files.writeString(nextIdDbPath, "17")
        IdService idService = new IdService(nextIdDbPath, new FilesService())

        expect:
        ['17'] == Files.readAllLines(nextIdDbPath)

        and:
        17L == idService.getNextIdAndIncrement()
        ['18'] == Files.readAllLines(nextIdDbPath)

        and:
        18L == idService.getNextIdAndIncrement()
        ['19'] == Files.readAllLines(nextIdDbPath)

        and:
        19L == idService.getNextIdAndIncrement()
        ['20'] == Files.readAllLines(nextIdDbPath)

    }

    def "empty file returns empty collection"() {
        expect:
        [] == Files.readAllLines(nextIdDbPath)
    }

}
