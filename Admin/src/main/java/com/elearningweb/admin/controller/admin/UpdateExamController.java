package com.elearningweb.admin.controller.admin;

import com.elearningweb.library.dto.CategoryDto;
import com.elearningweb.library.dto.ExamDto;
import com.elearningweb.library.service.impl.ExamServiceImpl;
import com.elearningweb.library.service.impl.FileServiceImpl;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class UpdateExamController {

    @Autowired
    private ExamServiceImpl examService;
    @Autowired
    private FileServiceImpl fileService;

    @GetMapping("/exams/all")
    public List<ExamDto> listExams() {
        return examService.findAllExams();
    }

    @GetMapping("/exams/year={year}")
    public ExamDto getExamByYear(@PathVariable("year") String year) {
        return examService.findByYear(year);
    }

    @GetMapping("/exams/year={year}/{category}")
    public ExamDto getExamByYearAndCategory(@PathVariable("year") String year,
                                 @PathVariable("category") String category) {
        return examService.findByYearAndCategory(year, category);
    }

    @GetMapping("/exams/id={id}")
    public ExamDto getExamById(@PathVariable("id") long id) {
        return examService.findById(id);
    }

    @GetMapping("/exams/year={year}/{category}/id={id}")
    public ExamDto getExamByYearAndCategoryAndId(@PathVariable("category") String category,
                                                 @PathVariable("id") long id,
                                                 @PathVariable("year") String year) {
        return examService.findByYearAndCategoryAndId(year, category, id);
    }

    @PostMapping(value = "/exams/save")
    @ResponseStatus(HttpStatus.CREATED)
    public ExamDto createExam(@RequestPart String title,
                              @RequestPart String category,
                              @RequestPart String description,
                              @RequestPart String year,
                              @RequestPart MultipartFile fileQuestion,
                              @RequestPart MultipartFile fileAnswer,
                              @RequestPart MultipartFile fileImage
    ) throws IOException {
        ExamDto examDto = new ExamDto();

        saveExam(fileAnswer, fileQuestion, fileImage, examDto);

        examDto.setTitle(title);
        examDto.setDescription(description);
        examDto.setYear(year);
        examDto.setCategory(new CategoryDto(null, category));


        return examService.save(examDto);
    }

    @PutMapping(value = "/exams/update/id={id}")
    public ExamDto updateExam(@RequestPart String title,
                              @RequestPart String category,
                              @RequestPart String description,
                              @RequestPart String year,
                              @Nullable @RequestPart MultipartFile fileQuestion,
                              @Nullable @RequestPart MultipartFile fileAnswer,
                              @RequestPart MultipartFile fileImage,
                              @PathVariable("id") long id) throws IOException {
        ExamDto examDto = examService.findById(id);
        if (examDto == null) {
            return null;
        } else {
            saveExam(fileAnswer, fileQuestion, fileImage, examDto);

            examDto.setTitle(title);
            examDto.setDescription(description);
            examDto.setYear(year);
            examDto.setCategory(new CategoryDto(null, category));
        }

        return examService.save(examDto);
    }

    @DeleteMapping(value = "/exams/delete/id={id}")
    public void deleteExam(@PathVariable("id") long id) {
        examService.delete(id);
    }

    private void saveExam(MultipartFile fileAnswer,
                          MultipartFile fileQuestion,
                          MultipartFile fileImage,
                          ExamDto examDto) {
        fileService.save(fileAnswer, FileServiceImpl.fileAnswerPath);
        examDto.setFileAnswer(FileServiceImpl.path.toString());

        fileService.save(fileQuestion, FileServiceImpl.fileQuestionPath);
        examDto.setFileQuestion(FileServiceImpl.path.toString());

        fileService.save(fileImage, FileServiceImpl.fileImagePath);
        examDto.setFileImage(FileServiceImpl.path.toString());
    }
}