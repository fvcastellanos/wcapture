package net.cavitos.wcapture.web.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AboutController {

    private final String gitBranch;
    private final String gitBuildHost;
    private final String gitBuildTime;
    private final String gitBuildUserEmail;
    private final String gitBuildUserName;
    private final String gitBuildVersion;
    private final String gitClosestTagCommitCount;
    private final String gitClosestTagName;
    private final String gitCommitId;
    private final String gitCommitIdAbbrev;
    private final String gitCommitMessageFull;
    private final String gitCommitMessageShort;
    private final String gitCommitTime;
    private final String gitCommitUserEmail;
    private final String gitCommitUserName;
    private final String gitDirty;
    private final String gitRemoteOriginUrl;
    private final String gitTags;

    public AboutController(@Value("${git.branch}") final String gitBranch,
                           @Value("${git.build.host}") final String gitBuildHost,
                           @Value("${git.build.time}") final String gitBuildTime,
                           @Value("${git.build.user.email}") final String gitBuildUserEmail,
                           @Value("${git.build.user.name}") final String gitBuildUserName,
                           @Value("${git.build.version}") final String gitBuildVersion,
                           @Value("${git.closest.tag.commit.count}") final String gitClosestTagCommitCount,
                           @Value("${git.closest.tag.name}") final String gitClosestTagName,
                           @Value("${git.commit.id}") final String gitCommitId,
                           @Value("${git.commit.id.abbrev}") final String gitCommitIdAbbrev,
                           @Value("${git.commit.message.full}") final String gitCommitMessageFull,
                           @Value("${git.commit.message.short}") final String gitCommitMessageShort,
                           @Value("${git.commit.time}") final String gitCommitTime,
                           @Value("${git.commit.user.email}") final String gitCommitUserEmail,
                           @Value("${git.commit.user.name}") final String gitCommitUserName,
                           @Value("${git.dirty}") final String gitDirty,
                           @Value("${git.remote.origin.url}") final String gitRemoteOriginUrl,
                           @Value("${git.tags}") final String gitTags) {
        this.gitBranch = gitBranch;
        this.gitBuildHost = gitBuildHost;
        this.gitBuildTime = gitBuildTime;
        this.gitBuildUserEmail = gitBuildUserEmail;
        this.gitBuildUserName = gitBuildUserName;
        this.gitBuildVersion = gitBuildVersion;
        this.gitClosestTagCommitCount = gitClosestTagCommitCount;
        this.gitClosestTagName = gitClosestTagName;
        this.gitCommitId = gitCommitId;
        this.gitCommitIdAbbrev = gitCommitIdAbbrev;
        this.gitCommitMessageFull = gitCommitMessageFull;
        this.gitCommitMessageShort = gitCommitMessageShort;
        this.gitCommitTime = gitCommitTime;
        this.gitCommitUserEmail = gitCommitUserEmail;
        this.gitCommitUserName = gitCommitUserName;
        this.gitDirty = gitDirty;
        this.gitRemoteOriginUrl = gitRemoteOriginUrl;
        this.gitTags = gitTags;
    }

    @GetMapping("/about")
    public String about(final Model model) {
        model.addAttribute("gitBranch", gitBranch);
        model.addAttribute("gitBuildHost", gitBuildHost);
        model.addAttribute("gitBuildTime", gitBuildTime);
        model.addAttribute("gitBuildVersion", gitBuildVersion);
        model.addAttribute("gitCommitId", gitCommitId);
        model.addAttribute("gitCommitIdAbbrev", gitCommitIdAbbrev);
        model.addAttribute("gitCommitTime", gitCommitTime);
        model.addAttribute("gitTags", gitTags);

        return "about";
    }

}
