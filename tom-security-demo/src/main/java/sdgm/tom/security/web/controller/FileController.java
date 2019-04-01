package sdgm.tom.security.web.controller;

import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sdgm.tom.security.dto.FileInfo;
import sun.nio.ch.IOUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;

@RestController
@RequestMapping("/file")
public class FileController {

    private String folder="C:\\Users\\Administrator\\Desktop\\tom-security\\tom-security-demo\\src\\main\\java\\sdgm\\tom\\security\\web\\controller";

    @PostMapping
    public FileInfo upload(MultipartFile file) throws IOException {
        System.out.println(file.getName());
        System.out.println(file.getOriginalFilename());
        System.out.println(file.getSize());

        File localFile=new File(folder,new Date().getTime()+".txt");
        file.transferTo(localFile);
//        file.getInputStream();
        return new FileInfo(localFile.getAbsolutePath());
    }

    @GetMapping("/{id}")
    public void download(@PathVariable String id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        try(
                InputStream inputStream=new FileInputStream(new File(folder,id+".txt"));
                OutputStream outputStream=response.getOutputStream();
                ){
            response.setContentType("application/x-download");
            response.setHeader("Content-Disposition","attachment;filename=test.txt");
            IOUtils.copy(inputStream,outputStream);
            outputStream.flush();

        }
    }

}
