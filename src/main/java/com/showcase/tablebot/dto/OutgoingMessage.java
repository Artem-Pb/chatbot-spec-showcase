package com.showcase.tablebot.dto;

import java.util.List;

public record OutgoingMessage(String recipientId,
                              String platformGroupId,
                              String text,
                              List<List<Button>> keyboard) {
}
