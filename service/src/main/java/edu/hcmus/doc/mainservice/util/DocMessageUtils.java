package edu.hcmus.doc.mainservice.util;

import edu.hcmus.doc.mainservice.model.enums.MESSAGE;
import java.text.MessageFormat;
import java.util.Locale;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DocMessageUtils {

  private static ResourceBundleMessageSource messageSource;

  @Autowired
  public void setMessageSource(ResourceBundleMessageSource messageSource) {
    DocMessageUtils.messageSource = messageSource;
  }

  /**
   * Get locale from request header
   */
  public static Locale getLocale() {
    return LocaleContextHolder.getLocale();
  }

  public static String getContent(MESSAGE messageKey) {
    return getContent(messageKey.name());
  }

  public static String getContent(String messageKey) {
    String content;
    try {
      Locale locale = getLocale();
      content = messageSource.getMessage(messageKey, null, locale);
    } catch (NoSuchMessageException e) {
      log.error(e.getMessage());
      content = getDefaultContent(messageKey);
    }
    return content;
  }

  public static String getContent(MESSAGE messageKey, Object... arguments) {
    return getContent(messageKey.name(), arguments);
  }

  public static String getContent(String messageKey, Object... arguments) {
    String content = getContent(messageKey);
    MessageFormat messageFormat = new MessageFormat(content);
    content = messageFormat.format(arguments);
    return content;
  }

  private static String getDefaultContent(String msgKey) {
    String content;
    try {
      Locale defaultLocale = new Locale("vi", "VN");
      content = messageSource.getMessage(msgKey, null, defaultLocale);
    } catch (NoSuchMessageException e) {
      content = msgKey;
    }
    return content;
  }
}
