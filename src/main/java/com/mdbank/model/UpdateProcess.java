package com.mdbank.model;

import com.mdbank.model.dto.Status;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.time.Instant;

import static javax.persistence.GenerationType.SEQUENCE;

@Slf4j
@Data
@Entity
@NoArgsConstructor
public class UpdateProcess {
    @Id
    @Column(name = "update_process_id")
    @GeneratedValue(strategy = SEQUENCE, generator = "update_process_seq_gen")
    @SequenceGenerator(name = "update_process_seq_gen", sequenceName = "update_process_id_seq", allocationSize = 1)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    private User user;
    private Instant date;
    @Enumerated(EnumType.STRING)
    private Status status;
    private String description;

    public UpdateProcess(User user) {
        this.user = user;
        date = Instant.now();
    }
}