package cat.tecnocampus.restController;

import cat.tecnocampus.domain.NoteLab;
import cat.tecnocampus.domain.UserLab;
import cat.tecnocampus.useCases.UserUseCases;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


@SpringBootApplication(exclude = {SecurityAutoConfiguration.class, ManagementWebSecurityAutoConfiguration.class})
@RestController
@EnableOAuth2Client
@Controller
public class GitHubRest {

	@Autowired
	OAuth2ClientContext oauth2ClientContext;

	@Autowired
	private UserUseCases useCases;

	private String url_GET_repositories = "https://api.github.com/user/repos";
    private String url_GET_gists = "https://api.github.com/gists";



	/* Get the repos */

	@GetMapping("/repositories/{name}")
	public ResponseEntity<String> getRepositories(@PathVariable String name) { //note that "name" is only used to store tokens

		ResponseEntity<String> response;

		if (!useCases.hasToken(name)) {
			OAuth2RestTemplate gitHubTemplate = gitHubRestTemplate();//new OAuth2RestTemplate(gitHub(), oauth2ClientContext);
			response = queryWithOauth2RestTemplate(gitHubTemplate);

			System.out.println("New name token: " + gitHubTemplate.getAccessToken().getValue());

			useCases.saveToken(name, gitHubTemplate.getAccessToken().getValue());
            useCases.saveTokenType(name, gitHubTemplate.getAccessToken().getTokenType());
		}
		else {
			response = queryWithPlainRestTEmplate(name);
			System.out.println("Known name token: " + useCases.getTok(name));
		}

		return response;
	}



	/* Get the Gists */

	@GetMapping("/gists/{name}")
	public ResponseEntity<String> getGists(@PathVariable String name) { //note that "name" is only used to store tokens

		ResponseEntity<String> response;

		if (!useCases.hasToken(name)) {
			OAuth2RestTemplate gitHubTemplate = gitHubRestTemplate();//new OAuth2RestTemplate(gitHub(), oauth2ClientContext);
			response = queryWithOauth2RestTemplateGists(gitHubTemplate);

			System.out.println("New name token: " + gitHubTemplate.getAccessToken().getValue());

            useCases.saveToken(name, gitHubTemplate.getAccessToken().getValue());
            useCases.saveTokenType(name, gitHubTemplate.getAccessToken().getTokenType());
		}
		else {
			response = queryWithPlainRestTEmplateGists(name);
			System.out.println("Known name token: " + useCases.getTok(name));
		}

		return response;
	}



	/* New Gists */

	@PostMapping("api/users/{user}/createNote")
	public ResponseEntity<String> createNote (@PathVariable(value = "user") String id, @RequestBody NoteLab noteLab){
		UserLab userLab = useCases.getUser(id);
		useCases.addUserNote(userLab, noteLab);


		ResponseEntity<String> response;

		if (!useCases.hasToken(id)) {
			OAuth2RestTemplate gitHubTemplate = gitHubRestTemplate();//new OAuth2RestTemplate(gitHub(), oauth2ClientContext);
			response = queryWithOauth2RestTemplateNewGists(gitHubTemplate, noteLab);

			System.out.println("New name token: " + gitHubTemplate.getAccessToken().getValue());

            useCases.saveToken(id, gitHubTemplate.getAccessToken().getValue());
            useCases.saveTokenType(id, gitHubTemplate.getAccessToken().getTokenType());
		}
		else {
			response = queryWithOauth2RestTemplateNewGists(id, noteLab);
			System.out.println("Known name token: " + useCases.getTok(id));
		}

		return response;

	}

	private ResponseEntity<String> queryWithOauth2RestTemplate(OAuth2RestTemplate gitHubTemplate) {
		//Asks for user authorization only if it hasn't got the token (user authorized gitHub)
		ResponseEntity<String> response =
				gitHubTemplate.exchange(url_GET_repositories, HttpMethod.GET, null, String.class);

		return response;
	}

	private ResponseEntity<String> queryWithOauth2RestTemplateGists(OAuth2RestTemplate gitHubTemplate) {
		//Asks for user authorization only if it hasn't got the token (user authorized gitHub)
		ResponseEntity<String> response =
				gitHubTemplate.exchange(this.url_GET_gists, HttpMethod.GET, null, String.class);

		return response;
	}

	private ResponseEntity<String> queryWithPlainRestTEmplateGists(String name) {
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		headers.add("Authorization", String.format("%s %s", useCases.getTokType(name), useCases.getTok(name)));
		headers.add("Accept", "application/vnd.github.v3+json");

		RestTemplate restTemplate = new RestTemplate();

		String body = "";
		HttpEntity<String> request = new HttpEntity<String>(body, headers);

		ResponseEntity<String> result = restTemplate.exchange(url_GET_gists,
				HttpMethod.GET, request, String.class);

		return result;
	}


    private ResponseEntity<String> queryWithOauth2RestTemplateNewGists(OAuth2RestTemplate gitHubTemplate, NoteLab noteLab) {
        //Asks for user authorization only if it hasn't got the token (user authorized gitHub)

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();

        headers.add("Authorization", String.format("%s %s", gitHubTemplate.getAccessToken()
                .getTokenType(), gitHubTemplate.getAccessToken().getValue()));

        headers.add("Accept", "application/vnd.github.v3+json");

        String body = "{\n" +
                "  \"description\": \"Gist created with Spring Oauth\",\n" +
                "  \"public\": true,\n" +
                "  \"files\": {\n" +
                "    \""+ noteLab.getTitle()+".txt\": {\n" +
                "      \"content\": \""+noteLab.getContent()+"\" \n" +
                "    }\n" +
                "  }\n" +
                "}";
        HttpEntity<String> request = new HttpEntity<String>(body, headers);

        ResponseEntity<String> response =
                gitHubTemplate.exchange(this.url_GET_gists, HttpMethod.POST, request, String.class);

        return response;
    }

	private ResponseEntity<String> queryWithOauth2RestTemplateNewGists(String name, NoteLab noteLab) {
		//Asks for user authorization only if it hasn't got the token (user authorized gitHub)

		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		headers.add("Authorization", String.format("%s %s", useCases.getTokType(name), useCases.getTok(name)));
		headers.add("Accept", "application/vnd.github.v3+json");

		String body = "{\n" +
				"  \"description\": \"Gist created with Spring Oauth\",\n" +
				"  \"public\": true,\n" +
				"  \"files\": {\n" +
				"    \""+ noteLab.getTitle()+".txt\": {\n" +
				"      \"content\": \""+noteLab.getContent()+"\" \n" +
				"    }\n" +
				"  }\n" +
				"}";
		HttpEntity<String> request = new HttpEntity<String>(body, headers);

		RestTemplate gitHubTemplate = new RestTemplate();

		ResponseEntity<String> response =
				gitHubTemplate.exchange(this.url_GET_gists, HttpMethod.POST, request, String.class);

		return response;
	}

	private ResponseEntity<String> queryWithPlainRestTEmplate(String name) {
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		headers.add("Authorization", String.format("%s %s", useCases.getTokType(name), useCases.getTok(name)));
		headers.add("Accept", "application/vnd.github.v3+json");

		RestTemplate restTemplate = new RestTemplate();

		String body = "";
		HttpEntity<String> request = new HttpEntity<String>(body, headers);

		ResponseEntity<String> result = restTemplate.exchange(url_GET_repositories,
				HttpMethod.GET, request, String.class);

		return result;
	}


	public static void main(String[] args) {
		SpringApplication.run(GitHubRest.class, args);
	}

	@Bean
	public OAuth2RestTemplate gitHubRestTemplate() {
		return new OAuth2RestTemplate(gitHub(), oauth2ClientContext);
	}

	@Bean
	@ConfigurationProperties("gitHub.client")
	public AuthorizationCodeResourceDetails gitHub() {
		return new AuthorizationCodeResourceDetails();
	}

	@Bean
	@ConfigurationProperties("gitHub.resource")
	public ResourceServerProperties gitHubResource() {
		return new ResourceServerProperties();
	}

}


