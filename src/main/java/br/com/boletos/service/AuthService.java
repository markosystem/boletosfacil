package br.com.boletos.service;

import br.com.boletos.application.Config;
import br.com.boletos.application.ResponseObject;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class AuthService {

    /**
     * Método que realiza a verificação do token passado via QueryString na URL. Caso não exista o token ou o
     * mesmo seja inválido, o retorno do método é falso. O retorno é verdadeiro caso contrário.
     *
     * @param request Este é carregado automaticamente a cada requisição
     * @return boolean
     */
    public static boolean isLoggedIn(HttpServletRequest request) throws SignatureException {
        if (request.getHeader("token") == null)
            return false;
        try {
            Jwts.parser().setSigningKey(Config.KEY_AUTHENTICATION).parseClaimsJws(request.getHeader("token"));
            return true;
        } catch (SignatureException e) {
            return false;
        }
    }

    public static Integer getIdUserLogged(String jwsHash) {
        try {
            Claims claims = Jwts.parser().setSigningKey(Config.KEY_AUTHENTICATION).parseClaimsJws(jwsHash).getBody();
            return Integer.parseInt(claims.getSubject());
        } catch (SignatureException e) {
            throw new SignatureException(null);
        }
    }

    /**
     * Retorno padrão a ser desserializado como JSON em caso de o usuário não estar autorizado a acessar
     * determinado recurso.
     *
     * @return ResponseObject
     */
    public static ResponseObject responseNotAuthorized() {
        return new ResponseObject(511, "Você precisa estar logado para acessar este recurso!", null);
    }

    /**
     * Método que gera um hash de determinada string com o algoritmo SHA-256. Utiliza do Config.KEY_AUTHENTICATION
     * como salt para maior segurança
     *
     * @param value contendo string base para gerar o hash
     * @return String Palavra contendo o hash da string passada
     */
    public static String generateHash(String value) {
        try {
            MessageDigest algorithm = MessageDigest.getInstance("SHA-256");
            byte messageDigest[] = algorithm.digest(value.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest)
                hexString.append(String.format("%02X", 0xFF & b));
            return hexString.toString();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

}
