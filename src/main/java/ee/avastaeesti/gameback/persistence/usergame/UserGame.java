package ee.avastaeesti.gameback.persistence.usergame;

import ee.avastaeesti.gameback.persistence.game.Game;
import ee.avastaeesti.gameback.persistence.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user_game", schema = "game")
public class UserGame {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @NotNull
    @Column(name = "total_locations", nullable = false)
    private Integer totalLocations;

    @NotNull
    @Column(name = "locations_answered", nullable = false)
    private Integer locationsAnswered;

    @NotNull
    @Column(name = "total_score", nullable = false)
    private Integer totalScore;

    @NotNull
    @Column(name = "correct_answers", nullable = false)
    private Integer correctAnswers;

    @NotNull
    @Column(name = "is_complete", nullable = false)
    private Boolean isComplete = false;
}