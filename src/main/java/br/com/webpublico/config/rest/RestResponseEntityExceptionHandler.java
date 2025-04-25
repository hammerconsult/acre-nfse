package br.com.webpublico.config.rest;

import br.com.webpublico.domain.dto.exception.NfseOperacaoNaoPermitidaException;
import br.com.webpublico.domain.dto.exception.TipoValidacao;
import br.com.webpublico.domain.dto.exception.ValidacaoNfseWSException;
import br.com.webpublico.exception.OperacaoNaoPermitidaException;
import br.com.webpublico.exception.RegistroExistenteException;
import br.com.webpublico.security.UserNotActivatedException;
import br.com.webpublico.util.errorvalidation.SimpleValidationErrorDTO;
import br.com.webpublico.util.errorvalidation.ValidationFieldErrorDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;


@ControllerAdvice
public class RestResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(RestResponseEntityExceptionHandler.class);

    private MessageSource messageSource;

    @Autowired
    public RestResponseEntityExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationFieldErrorDTO processValidationError(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        return processFieldErrors(fieldErrors);
    }


    @ExceptionHandler(RegistroExistenteException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public SimpleValidationErrorDTO integrityError(RegistroExistenteException ex) {
        return new SimpleValidationErrorDTO("RegistroExistente", ex.getMessage());
    }

    @ExceptionHandler(OperacaoNaoPermitidaException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public OperacaoNaoPermitidaException campoObrigatorioException(OperacaoNaoPermitidaException ex) {
        return new OperacaoNaoPermitidaException(ex.getMessage(), ex.getMensagens());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    @ExceptionHandler(AuthenticationServiceException.class)
    protected ValidationFieldErrorDTO handleAuthenticationServiceException(AuthenticationServiceException exception) {
        logger.debug("Caiu aqui em AuthenticationServiceException");
        ValidationFieldErrorDTO ved = new ValidationFieldErrorDTO();
        ved.addFieldError("user", "login", "Usuário/Senha Inválido", "403");
        return ved;
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    @ExceptionHandler(UsernameNotFoundException.class)
    protected ValidationFieldErrorDTO handleUserNotFoundException(UsernameNotFoundException exception) {
        logger.debug("Caiu aqui em AuthenticationServiceException");
        ValidationFieldErrorDTO ved = new ValidationFieldErrorDTO();
        ved.addFieldError("user", "login", "Usuário/Senha Inválido", "403");
        return ved;
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(UserNotActivatedException.class)
    protected SimpleValidationErrorDTO handleUserNotActivatedException(UserNotActivatedException exception) {
        logger.debug("Caiu aqui em handleUserNotActivatedException");
        return new SimpleValidationErrorDTO("UsuarioNaoAtivado", exception.getMessage());
    }


    @ExceptionHandler(NfseOperacaoNaoPermitidaException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public SimpleValidationErrorDTO processarErroDeValidacao(NfseOperacaoNaoPermitidaException ex) {
        ResponseEntity.BodyBuilder builder = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
        String mensagem = String.join("<br/>", ex.getMensagens());
        return new SimpleValidationErrorDTO("Warning", mensagem);
    }

    @ExceptionHandler(ValidacaoNfseWSException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public SimpleValidationErrorDTO processarErroDeValidacaoWs(ValidacaoNfseWSException ex) {
        ResponseEntity.BodyBuilder builder = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
        String mensagem = String.join("<br/>", ex.getValidacoes().stream().map(TipoValidacao::getDescricao).collect(Collectors.toList()));
        return new SimpleValidationErrorDTO("Warning", mensagem);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public SimpleValidationErrorDTO processarErroDeValidacao(HttpClientErrorException ex) {
        if (ex.getStatusCode().compareTo(HttpStatus.NOT_FOUND) == 0) {
            return new SimpleValidationErrorDTO("Operação Não Realizada", "Não foi possível estabelecer a conexão com o WebPublico.");
        }

        String[] strings = ex.getResponseHeaders().get("nfse-message-error").toArray(new String[0]);
        String mensagem = String.join("<br/>", strings);
        return new SimpleValidationErrorDTO("Warning", mensagem);

    }

    private ValidationFieldErrorDTO processFieldErrors(List<FieldError> fieldErrors) {
        ValidationFieldErrorDTO dto = new ValidationFieldErrorDTO();
        for (FieldError fieldError : fieldErrors) {
            String localizedErrorMessage = resolveLocalizedErrorMessage(fieldError);
            dto.addFieldError(fieldError.getObjectName(), fieldError.getField(), localizedErrorMessage, fieldError.getCode());
        }

        return dto;
    }

    private String resolveLocalizedErrorMessage(FieldError fieldError) {
        Locale currentLocale = LocaleContextHolder.getLocale();
        logger.debug("CurrentLocale: " + currentLocale);
        logger.debug("fieldError.getField(): " + fieldError.getField());
        logger.debug("fieldError.getCode(): " + fieldError.getCode());
        logger.debug("fieldError.getObjectName(): " + fieldError.getObjectName());
        logger.debug("fieldError.getDefaultMessage(): " + fieldError.getDefaultMessage());
        String localizedErrorMessage = messageSource.getMessage(fieldError, currentLocale);
        logger.debug("localizedErrorMessage: " + localizedErrorMessage);
        //If the message was not found, return the most accurate field error code instead.
        //You can remove this check if you prefer to get the default error message.
        if (localizedErrorMessage.equals(fieldError.getDefaultMessage())) {
            String[] fieldErrorCodes = fieldError.getCodes();
            localizedErrorMessage = fieldErrorCodes[0];
        }
        logger.debug("localizedErrorMessage2: " + localizedErrorMessage);
        return localizedErrorMessage;
    }


}
