package ee.avastaeesti.gameback.persistence.game;

import ee.avastaeesti.gameback.controller.game.dto.GameInfo;
import ee.avastaeesti.gameback.controller.game.dto.NewGame;
import ee.avastaeesti.gameback.controller.game.dto.UserGame;
import ee.avastaeesti.gameback.status.Status;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, imports = {Status.class})
public interface GameMapper {

    @Mapping(source = "gameName", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "timePerLocation", target = "timePerLocation")
    @Mapping(expression = "java(Status.ACTIVE.getCode())", target = "status")
    Game toGame(NewGame newGame);

    @Mapping(source = "id", target = "gameId")
    @Mapping(source = "name", target = "gameName")
    @Mapping(source = "description", target = "gameDescription")
    UserGame toUserGame(Game game);

    List<UserGame> toUserGames(List<Game> games);

    @InheritConfiguration(name = "toUserGame")
    @Mapping(constant = "0", target = "totalTopScore")
    @Mapping(constant = "", target = "username")
    GameInfo toGameInfo(Game game);

    List<GameInfo> toGameInfos(List<Game> games);
}