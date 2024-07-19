package sfera.payload;

import lombok.*;

public interface StudentRatingDTO {
    String getFirstname();
    String getLastname();
    String getPhoneNumber();
    int getScore();
}
