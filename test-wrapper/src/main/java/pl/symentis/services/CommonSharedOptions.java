package pl.symentis.services;

public record CommonSharedOptions(String commitSha, int runAttempt, String jvmArgs, String testNameRegex) {}
