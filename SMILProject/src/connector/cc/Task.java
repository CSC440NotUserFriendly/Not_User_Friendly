package connector.cc;

import java.io.File;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Task {

  private Date insertDate;
  private String emailAddress;
  

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private String id;

  private Boolean done = Boolean.FALSE;
  private String userId;
  private String recipient;
  private File file;

  public Task() {
  }

  public Date getInsertDate() {
    return insertDate;
  }

  public String getEmailAddress() {
    return this.emailAddress;
  }
  
  public String getRecipient() {
	  return recipient;
  }

  public String getId() {
    return id;
  }

  public Boolean isDone() {
    return done;
  }

  public String getUserId() {
    return userId;
  }
  
  public File getFile() {
    return file;
  }

  public void setRecipient(String recipient) {
	  this.recipient = recipient;
  }
  public void setInsertDate(Date insertDate) {
    this.insertDate = insertDate;
  }

  public void setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
  }

  public void setId(String id) {
    this.id = id;
  }
  
  public void setFile(File file) {
            this.file = file;
          }

  public void setDone(Boolean done) {
    this.done = done;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("Task [insertDate=");
    builder.append(insertDate);
    builder.append(", done=");
    builder.append(done);
    builder.append(", file=");
    builder.append(file);
    builder.append("]");
    return builder.toString();
  }
}