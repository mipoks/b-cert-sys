package design.kfu.sunrise.requestlimiter.service;

import design.kfu.sunrise.requestlimiter.annotation.Limit;

import jakarta.servlet.http.HttpServletRequest;

/**
 * @author Daniyar Zakiev
 */
public interface LimitationService {
    boolean checkRequestLimit(Limit limit, HttpServletRequest request);
}
