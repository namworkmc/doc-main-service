package edu.hcmus.doc.mainservice.util;

import edu.hcmus.doc.mainservice.model.dto.TokenDto;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface KeycloakResource {

  @POST
  TokenDto getToken(
      @FormParam("grant_type") String grantType,
      @FormParam("username") String username,
      @FormParam("password") String password,
      @FormParam("scope") String scope,
      @FormParam("client_id") String clientId,
      @FormParam("client_secret") String clientSecret
  );

  @POST
  TokenDto refreshToken(
      @FormParam("grant_type") String grantType,
      @FormParam("refresh_token") String refreshToken,
      @FormParam("scope") String scope,
      @FormParam("client_id") String clientId,
      @FormParam("client_secret") String clientSecret
  );
}
