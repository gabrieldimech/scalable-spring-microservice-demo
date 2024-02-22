package gatling.simulation;

import io.gatling.javaapi.core.CoreDsl;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpDsl;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.time.Duration;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static io.gatling.javaapi.core.CoreDsl.StringBody;
import static io.gatling.javaapi.core.CoreDsl.constantUsersPerSec;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class PlayerRequestSimulation extends Simulation {
    HttpProtocolBuilder httpProtocol = HttpDsl.http
            .baseUrl("http://localhost:8080")
            .acceptHeader("application/json")
            .userAgentHeader("Gatling/Performance Test");

    Iterator<Map<String, Object>> feeder =
            Stream.generate((Supplier<Map<String, Object>>) ()
                    -> Map.of(
                    "playerId", UUID.randomUUID().toString(),
                    "name", UUID.randomUUID().toString(),
                    "surname", UUID.randomUUID().toString(),
                    "username", UUID.randomUUID().toString(),
                    "email", UUID.randomUUID().toString(),
                    "betReferenceId", UUID.randomUUID().toString()
                    )
            ).iterator();

    ScenarioBuilder scn = CoreDsl.scenario("Load Test Creating Players")
            .feed(feeder)
            .exec(http("create-player-request")
                    .post("/player")
                    .header("Content-Type", "application/json")
                    .body(StringBody("{ \"id\": \"#{playerId}\", \"name\": \"#{name}\", \"surname\": \"#{surname}\" , \"username\": \"#{username}\", \"email\": \"#{email}\"}"))
                  //  .body(StringBody("{ \"id\": \"#{id}\", \"name\": \"#{name}\", \"surname: \"#{surname}\", \"username\": \"#{username}\", \"email\": \"#{email}\" }"))
                    .check(status().is(201))
                    //.check(jsonPath("$.data.id").saveAs("playerId"))
                    //.check(header("playerId").saveAs("playerId"))
            )
            .exec(http("get-player-request")
                    .get(session -> "/player/" + session.getString("playerId"))
                    .check(status().is(200))
            )
            .exec(http("play-game")
                    .post(session -> "/games/NUMBERS_GAME")
                    .header("Content-Type", "application/json")
                    .body(StringBody("{ \"playerId\": \"#{playerId}\", \"betReferenceId\": \"#{betReferenceId}\", \"betAmount\": \"10\" , \"betNumber\": \"7\"}"))
                    .check(status().is(200))
            )
            .exec(http("get-leaderboard")
                    .get(session -> "/leaderBoard")
                    .check(status().is(200))
            );

    public PlayerRequestSimulation() {
        this.setUp(scn.injectOpen(constantUsersPerSec(100).during(Duration.ofSeconds(10))))
                .protocols(httpProtocol);
    }
}
