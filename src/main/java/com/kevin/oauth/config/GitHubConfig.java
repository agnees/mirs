package com.kevin.oauth.config;


import com.kevin.oauth.api.GitHubApi;
import com.kevin.oauth.service.GithubOAuthService;
import com.kevin.oauth.service.OAuthServiceDecorator;
import org.scribe.builder.ServiceBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GitHubConfig {

    @Value("${oAuth.callbackUrl}") String CALLBACK_URL;
    @Value("${oAuth.host}") String host;

    @Value("${oAuth.github.status}") String status;
    @Value("${oAuth.github.clientId}") String clientId;
    @Value("${oAuth.github.clientSecret}") String clientSecret;



    @Bean
    public GitHubApi githubApi(){
        return new GitHubApi(status);
    }

    @Bean
    public OAuthServiceDecorator getGithubOAuthService(){
        return new GithubOAuthService(new ServiceBuilder()
                .provider(githubApi())
                .apiKey(clientId)
                .apiSecret(clientSecret)
                .callback(String.format(CALLBACK_URL, host, OAuthTypes.GITHUB))
                .build());
    }

    public static void main(String[] args) {
        String CB = "%s/oauth/%s/callback";
        String host = "http://10.161.104.161:8080/api";
        System.out.println(String.format(CB,host,OAuthTypes.GITHUB));

    }

}
