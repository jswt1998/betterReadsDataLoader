package com.example.demo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.example.demo.author.Author;
import com.example.demo.author.AuthorRepository;

@SpringBootApplication
@EnableConfigurationProperties(DataStaxConnectionProperties.class)
public class BetterreadsDataLoaderApplication {

	@Autowired	AuthorRepository authorRepository;
	
	@Value("${datadump.location.author}")
	private String authorDumpLocation;
	
	@Value("${datadump.location.works}")
	private String workDumpLocation;
	
	public static void main(String[] args) {
		SpringApplication.run(BetterreadsDataLoaderApplication.class, args);
	}
	
	@PostConstruct
	public void start() {
		Path path =Paths.get(authorDumpLocation);
		
		try(Stream<String> lines = Files.lines(path)){
			lines.forEach(line-> {
				String jsonString = line.substring(line.indexOf("{"));
								
				try {
					JSONObject jsonObject = new JSONObject(jsonString);
					
					Author author = new Author();
					author.setName(jsonObject.optString("name"));
					author.setPersonalName(jsonObject.optString("personal_name"));
					author.setId(jsonObject.optString("key").replace("/authors/", ""));
				
					authorRepository.save(author);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
				
				
			});
			
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	@Bean
    public CqlSessionBuilderCustomizer sessionBuilderCustomizer(DataStaxConnectionProperties astraProperties) {
        Path bundle = astraProperties.getSecureConnectBundle().toPath();
        return builder -> builder.withCloudSecureConnectBundle(bundle);
    }

}
