package com.mdsol.checkmeoutbot.app.models

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.mdsol.checkmeoutbot.app.models.GithubModels.{Head, Links, PullRequestEvent}
import com.mdsol.checkmeoutbot.app.models.SlackModels.EventVerification
import github4s.free.domain.Repository
import spray.json.{DefaultJsonProtocol, JsArray, JsNumber, JsString, JsValue, RootJsonFormat}

object GithubModels {

  case class Authenticate(client_id: String, client_secret: String, code: String)


  case class Config(
                     content_type: String,
                     insecure_ssl: String,
                     url: String
                   )

  case class LastResponse(
                           code: Option[Int],
                           status: String,
                           message: Option[String]
                         )

  case class Webhook(
                      `type`: String,
                      id: Int,
                      name: String,
                      active: Boolean,
                      events: Seq[String],
                      config: Option[Config],
                      updated_at: String,
                      created_at: String,
                      url: String,
                      test_url: String,
                      ping_url: String,
                      last_response: Option[LastResponse]
                    )

  case class NewWebhookRequest(
                                name: String,
                                active: Boolean,
                                events: Seq[String],
                                config: Option[Config]
                              )

  case class Head(
                   label: String,
                   ref: String,
                   sha: String,
                   user: User,
                   repo: Repo
                 )

  case class Links(
                    self: Self,
                    html: Self,
                    issue: Self,
                    comments: Self,
                    review_comments: Self,
                    review_comment: Self,
                    commits: Self,
                    statuses: Self
                  )

  case class PullRequest(
                          url: String,
                          id: Int,
                          node_id: String,
                          html_url: String,
                          diff_url: String,
                          patch_url: String,
                          issue_url: String,
                          number: Int,
                          state: String,
                          locked: Boolean,
                          title: String,
                          user: User,
                          body: String,
                          created_at: String,
                          updated_at: String,
                          closed_at: Option[String],
                          merged_at: Option[String],
                          merge_commit_sha: Option[String],
                          assignee: Option[User],
                          assignees: Seq[Option[User]],
                          requested_reviewers: Seq[Option[User]],
                          requested_teams: Seq[Option[Team]],
                          labels: Option[Seq[Label]],
                          milestone: Option[Milestone],
                          commits_url: String,
                          review_comments_url: String,
                          review_comment_url: String,
                          comments_url: String,
                          statuses_url: String,
                          head: Head,
                          base: Head,
                          _links: Links,
                          author_association: String,
                          merged: Boolean,
                          mergeable: Option[Boolean],
                          rebaseable: Option[Boolean],
                          mergeable_state: String,
                          merged_by: Option[User],
                          comments: Int,
                          review_comments: Int,
                          maintainer_can_modify: Boolean,
                          commits: Int,
                          additions: Int,
                          deletions: Int,
                          changed_files: Int
                        )

  case class Repo(
                   id: Int,
                   node_id: String,
                   name: String,
                   full_name: String,
                   `private`: Boolean,
                   owner: User,
                   html_url: String,
                   description: Option[String],
                   fork: Boolean,
                   url: String,
                   forks_url: String,
                   keys_url: String,
                   collaborators_url: String,
                   teams_url: String,
                   hooks_url: String,
                   issue_events_url: String,
                   events_url: String,
                   assignees_url: String,
                   branches_url: String,
                   tags_url: String,
                   blobs_url: String,
                   git_tags_url: String,
                   git_refs_url: String,
                   trees_url: String,
                   statuses_url: String,
                   languages_url: String,
                   stargazers_url: String,
                   contributors_url: String,
                   subscribers_url: String,
                   subscription_url: String,
                   commits_url: String,
                   git_commits_url: String,
                   comments_url: String,
                   issue_comment_url: String,
                   contents_url: String,
                   compare_url: String,
                   merges_url: String,
                   archive_url: String,
                   downloads_url: String,
                   issues_url: String,
                   pulls_url: String,
                   milestones_url: String,
                   notifications_url: String,
                   labels_url: String,
                   releases_url: String,
                   deployments_url: String,
                   created_at: String,
                   updated_at: String,
                   pushed_at: String,
                   git_url: String,
                   ssh_url: String,
                   clone_url: String,
                   svn_url: String,
                   homepage: Option[String],
                   size: Int,
                   stargazers_count: Int,
                   watchers_count: Int,
                   language: Option[String],
                   has_issues: Boolean,
                   has_projects: Boolean,
                   has_downloads: Boolean,
                   has_wiki: Boolean,
                   has_pages: Boolean,
                   forks_count: Int,
                   mirror_url: Option[String],
                   archived: Boolean,
                   open_issues_count: Int,
                   license: Option[String],
                   forks: Int,
                   open_issues: Int,
                   watchers: Int,
                   default_branch: String
                 )

  case class PullRequestEvent(
                               action: String,
                               number: Int,
                               pull_request: PullRequest,
                               repository: Repo,
                               sender: User
                             )

  case class Self(
                   href: String
                 )

  case class User(
                   login: String,
                   id: Int,
                   node_id: String,
                   avatar_url: String,
                   gravatar_id: String,
                   url: String,
                   html_url: String,
                   followers_url: String,
                   following_url: String,
                   gists_url: String,
                   starred_url: String,
                   subscriptions_url: String,
                   organizations_url: String,
                   repos_url: String,
                   events_url: String,
                   received_events_url: String,
                   `type`: String,
                   site_admin: Boolean
                 )

  case class Label(
                    id: Int,
                    node_id: String,
                    url: String,
                    name: String,
                    description: Option[String],
                    color: String,
                    default: Boolean
                  )

  case class Organization(
                           login: String,
                           id: Int,
                           node_id: String,
                           url: String,
                           repos_url: String,
                           events_url: String,
                           hooks_url: String,
                           issues_url: String,
                           members_url: String,
                           public_members_url: String,
                           avatar_url: String,
                           description: String,
                           name: String,
                           company: String,
                           blog: String,
                           location: String,
                           email: String,
                           is_verified: Boolean,
                           has_organization_projects: Boolean,
                           has_repository_projects: Boolean,
                           public_repos: Int,
                           public_gists: Int,
                           followers: Int,
                           following: Int,
                           html_url: String,
                           created_at: String,
                           `type`: String
                         )

  case class Team(
                   id: Int,
                   node_id: String,
                   url: String,
                   name: String,
                   slug: String,
                   description: String,
                   privacy: String,
                   permission: String,
                   members_url: String,
                   repositories_url: String,
                   parent: Option[String],
                   members_count: Int,
                   repos_count: Int,
                   created_at: String,
                   updated_at: String,
                   organization: Organization
                 )

  case class Milestone (
                         url: String,
                         html_url: String,
                         labels_url: String,
                         id: Int,
                         node_id: String,
                         number: Int,
                         title: String,
                         description: Option[String],
                         creator: User,
                         open_issues: Int,
                         closed_issues: Int,
                         state: String,
                         created_at: String,
                         updated_at: String,
                         due_on: Option[String],
                         closed_at: Option[String]
                       )

  case class GetGithubAccess()


}