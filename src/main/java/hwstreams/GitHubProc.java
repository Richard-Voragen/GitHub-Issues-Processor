package hwstreams;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GitHubProc {
  public static Long getWordCount(Stream<GitHubComment> stream, String word) {
    Stream<Long> nums = stream.map(i -> countWordOccurancesInSingleComment(i, word));
    return nums.reduce((long)0, Long::sum);
  }

  private static Long countWordOccurancesInSingleComment(GitHubComment comment, String word) {
    String[] words = Util.getWords(comment.body());
    return Arrays.stream(words).filter(i -> i.equals(word)).count();
  }

  public static Map<String, Long> getPerProjectCount(Stream<GitHubComment> stream) {
    return stream.collect(Collectors.groupingBy(
            GitHubProc::getProject, Collectors.counting()));
  }
  public static String getProject(GitHubComment input) {
    String[] tokens = input.url().split("/");
    return (tokens[3] + "/" + tokens[4]);
  }

  public static Map<String, Long> getAuthorActivity(Stream<GitHubComment> stream) {
    return stream.collect(Collectors.groupingBy(
                    GitHubComment::author, Collectors.counting()));
  }

  public static Map<String, Long> getCommentUrlAuthorCount(Stream<GitHubComment> stream) {
    return filterCommentsWithUrl(stream)
            .collect(Collectors.groupingBy(
                    GitHubComment::author, Collectors.counting()));
  }

  public static Stream<GitHubComment> filterCommentsWithUrl(Stream<GitHubComment> comments) {
    return comments.filter(i -> i.body().contains("http://") || i.body().contains("https://"));
  }

  public static Map<String, Double> getAuthorAverageVerbosity(Stream<GitHubComment> stream) {
    return stream.collect(Collectors.groupingBy(
                    GitHubComment::author, Collectors.averagingDouble(
                            GitHubProc::countWords)));
  }

  private static double countWords(GitHubComment sentence) {
    String[] words = Util.getWords(sentence.body());
    return words.length;
  }

  public static Map<String, Map<String, Long>> getAuthorWordCountPerProject(
      Stream<GitHubComment> stream, String word) {
    return filterCommentsWithName(stream, word).collect(Collectors.groupingBy(
            GitHubProc::getProject, Collectors.groupingBy(
                    GitHubComment::author, Collectors.summingLong(
                            num -> countWordOccurancesInSingleComment((GitHubComment) num, word)))));
  }

  public static Stream<GitHubComment> filterCommentsWithName(Stream<GitHubComment> comments, String word) {
    return comments.filter(i -> i.body().contains(word));
  }
}
