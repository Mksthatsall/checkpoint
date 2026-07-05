package com.mks.projects.checkpointapp.service;

import com.mks.projects.checkpointapp.entity.Booking;

public interface CheckoutService {

    String getCheckOutSession(Booking booking, String successUrl, String failureUrl);
}
