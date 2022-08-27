package pl.futurecollars.invoicing.db.file

import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Path

class FilesServiceTest extends Specification {

    FilesService filesService = new FilesService()
    Path testingFilePath = File.createTempFile('testingFile','.txt').toPath()
    String line = "Testing line"

    def "should append line to a file"() {
        when:
        filesService.appendLineToFile(testingFilePath,line)
        String result = Files.readString(testingFilePath)

        then:
        result == line + System.lineSeparator()
    }

    def "should write lines to a file"() {
        given:
        List <String> lines = List.of(line,line,line)

        when:
        filesService.writeLinesToFile(testingFilePath,lines)
        List <String> result = Files.readAllLines(testingFilePath)

        then:
        result == lines
    }
}