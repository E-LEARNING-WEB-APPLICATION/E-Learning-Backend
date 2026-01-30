package com.learnease.server.events;

import java.util.UUID;

public record BookingPaidEvent(UUID bookingId)
{ }
