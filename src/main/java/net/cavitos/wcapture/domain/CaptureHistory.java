package net.cavitos.wcapture.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "capture_history")
@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CaptureHistory {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column
    private long id;

    @Column
    private String url;

    @Column
    private String requestId;

    @Column
    private String result;

    @Column
    private String storedPath;

    @Column
    private String error;

}
