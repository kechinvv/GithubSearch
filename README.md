# GithubSearch

## Table of contents

1. [General info](#general-info)
2. [Get started](#get-started)
3. [Usage](#usage)
4. [Checks](#checks)
5. [License](#license)

## General info <a name="general-info"></a>

This program allows you to collect projects with the required conditions. You can also implement your own checks and use
the library's methods for your own purposes.

## Get started <a name="get-started"></a>

For example, you can use GithubSearch as a submodule in your program. But in this case, you need to remove the line **
id 'application'** in build.gradle of GithubSearch.

```
git submodule add https://github.com/kechinvv/GithubSearch destination_folder
```

## Usage <a name="usage"></a>

Example of usage is located in src/main/kotlin/com.kechinvv.ghsearch/main. To iterate through the repositories, use
RepIterator. To configure the search, the following fields are provided:

```
 public RepIterator(int limit, String keywords, SortType sort, OrderType order)
```

1. int limit - Limit of repositories for iterator operation.
2. String keywords - keywords for search, separated by a space or plus.
3. SortType sort - sort by stars, forks, best matches, updates or help wanted issues.
4. OrderType order - ascending or descending sorting.

RepIterator.next return a RemoteRepository. In order to clone a repository for running checks on it call
remoteRep.cloneTo(Path path). This method return LocalRepository.

The necessary data for performing checks can be obtained as follows:

```
PSICreator().getPSIForProject(String path_to_project)
```

This return to you Pair<KtFile (contains PSI), BindingContext>. With this data, you can call checks, see what they
return, and delete unnecessary repositories.

## Checks <a name="checks"></a>

Currently, three checks are implemented: 
1. CheckRecursion (return list of recursive functions) 
2. CheckFieldChange (return list of changed fields)
3. CheckOverrideFun (return list of override functions) 
   
You can implement interface Check and create your own check.

## License <a name="license"></a>

Apache License 2.0
