package com.accounting.controller;

import com.accounting.service.AccountSymbolsService;
import com.accounting.service.ExceptionsService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import static com.accounting.config.Utils.*;

@Controller
@RequestMapping("resource")
public class ResourceController {

    private static final Logger logger = LogManager.getLogger(ResourceController.class);

    private AccountSymbolsService accountSymbolsService;
    private ExceptionsService exceptionsService;

    @Autowired
    ResourceController(AccountSymbolsService accountSymbolsService, ExceptionsService exceptionsService) {
        this.accountSymbolsService = accountSymbolsService;
        this.exceptionsService = exceptionsService;
    }

    @GetMapping(value = "/get/accountSymbols")
    public ResponseEntity<InputStreamResource> getClassSymbolsRequest(HttpServletRequest request) {
        logger.info(USER_MESSAGE_LOG,
                request.getRemoteUser(), request.getRemoteAddr(), request.getMethod(), request.getRequestURI());
        InputStream classSymbolsStream = accountSymbolsService.readFile();
        ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.status(Objects.nonNull(classSymbolsStream) ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR);
        return bodyBuilder.body(new InputStreamResource(Objects.isNull(classSymbolsStream) ? new ByteArrayInputStream(EXCEPTIONS_GET_ERROR_MESSAGE.getBytes()) : classSymbolsStream));
    }

    @PostMapping(value = "/add/accountSymbols")
    public ResponseEntity<String> addClassSymbolsRequest(@RequestBody(required = false) InputStreamResource resource, HttpServletRequest request) throws IOException {
        logger.info(USER_MESSAGE_LOG,
                request.getRemoteUser(), request.getRemoteAddr(), request.getMethod(), request.getRequestURI());
        boolean result = accountSymbolsService.write(resource.getInputStream());
        ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.status(result ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR);
        return bodyBuilder.body(result ? ACCOUNT_ADD_SYMBOLS_SUCCESS_MESSAGE : ACCOUNT_ADD_SYMBOLS_ERROR_MESSAGE);
    }

    @GetMapping(value = "/get/exceptions")
    public ResponseEntity<InputStreamResource> getExceptionsRequest(HttpServletRequest request) {
        logger.info(USER_MESSAGE_LOG,
                request.getRemoteUser(), request.getRemoteAddr(), request.getMethod(), request.getRequestURI());
        InputStream exceptionsStream = exceptionsService.readFile();
        ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.status(Objects.nonNull(exceptionsStream) ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR);
        return bodyBuilder.body(new InputStreamResource(Objects.isNull(exceptionsStream) ? new ByteArrayInputStream(ACCOUNT_GET_SYMBOLS_ERROR_MESSAGE.getBytes()) : exceptionsStream));
    }

    @PostMapping(value = "/add/exceptions")
    public ResponseEntity<String> addExceptionsRequest(@RequestBody(required = false) InputStreamResource resource, HttpServletRequest request) throws IOException {
        logger.info(USER_MESSAGE_LOG,
                request.getRemoteUser(), request.getRemoteAddr(), request.getMethod(), request.getRequestURI());
        boolean result = exceptionsService.write(resource.getInputStream());
        ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.status(result ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR);
        return bodyBuilder.body(result ? EXCEPTIONS_ADD_SUCCESS_MESSAGE : EXCEPTIONS_ADD_ERROR_MESSAGE);
    }
}
