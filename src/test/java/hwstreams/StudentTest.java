package hwstreams;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class StudentTest {
    Stream<GitHubComment> getTestData() {
        return Stream.of(
                new GitHubComment(
                        "22422659",
                        "b6af12b3e4586e24d88ae9843d731926c4070c25",
                        "https://github.com/google/design/pull/2826#discussion_r22422659",
                        "Richard Voragen",
                        "2015-01-02T21:00:12Z",
                        "updated https://google.com and http://google.com, added bug"),
                new GitHubComment(
                        "22422662",
                        "c6343604d6532d85cd531dcc7336d4626cbeba2e",
                        "https://github.com/google/design/pull/376#discussion_r22422662",
                        "Professor Posnett",
                        "2015-01-02T21:00:14Z",
                        "bug fixes"
                                + "new design for http://google.com/"),
                new GitHubComment(
                        "22422664",
                        "c6333604d6532d85cd531dcc7336d4626cbeba2e",
                        "https://github.com/bing/better/pull/376#discussion_r22422662",
                        "Professor Posnett",
                        "2015-01-02T21:00:14Z",
                        "Updated Readme, bug fixes for all of the bugs completed with my pet bug named bug"
                                + " beat https://google.com with https://bing.com"));
    }

    @Test
    public void studentShouldTest() { // TODO: rename test name to something meaningful
        // This part tests if it counts multiple occurances of a word in data
        var testMap = GitHubProc.getAuthorWordCountPerProject(getTestData(), "bug");
        var expectedProjectMap = new HashMap<String, Map<String, Long>>();
        expectedProjectMap.put(
                "google/design",
                new HashMap<String, Long>() {
                    {
                        put("Richard Voragen", 1L);
                        put("Professor Posnett", 1L);
                    }
                });
        expectedProjectMap.put(
                "bing/better",
                new HashMap<String, Long>() {
                    {
                        put("Professor Posnett", 3L);
                    }
                });
        assertEquals(expectedProjectMap, testMap);
    }
}
