package com.hcltech.busbooking.exception;


import com.hcltech.busbooking.dto.ErrorMessage;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class ControllerExceptionHandlerTest {

    private ControllerExceptionHandler controllerExceptionHandler = new ControllerExceptionHandler();

    @Test
    public void testHandleNoSeatExistsException() {
        NoSeatExistsException ex = new NoSeatExistsException("No seats");
        WebRequest webRequest = mock(WebRequest.class);
        ResponseEntity<ErrorMessage> response = controllerExceptionHandler.noSeatExistsException(ex,webRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("No seats", response.getBody().getMessage());
    }

    @Test
    public void testHandleBookingCancelledException() {
        BookingCancelledException ex = new BookingCancelledException("Already cancelled");
        WebRequest webRequest = mock(WebRequest.class);
        ResponseEntity<ErrorMessage> response = controllerExceptionHandler.bookingCancelledException(ex,webRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Already cancelled", response.getBody().getMessage());
    }

    @Test
    public void testHandleGlobalExceptionHandler() {
        Exception ex = new Exception("Something went wrong");
        WebRequest webRequest = mock(WebRequest.class);
        ResponseEntity<ErrorMessage> response = controllerExceptionHandler.globalExceptionHandler(ex,webRequest);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Something went wrong", response.getBody().getMessage());
    }
}
