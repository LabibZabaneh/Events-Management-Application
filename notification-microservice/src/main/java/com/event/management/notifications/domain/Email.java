package com.event.management.notifications.domain;

import java.util.List;

public interface Email {

    String getRecipient();
    List<String> getCc();
    List<String> getBcc();
    String getSubject();
    String getHtmlBody();
    String getTextBody();
    String getReplyTo();

}
