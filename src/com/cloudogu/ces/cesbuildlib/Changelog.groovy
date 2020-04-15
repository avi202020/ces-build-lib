package com.cloudogu.ces.cesbuildlib

class Changelog implements Serializable {

	String getChangelog(String releaseVersion){
	    start = getChangelogStartIndex(releaseVersion)
	    end = getChangelogEndIndex(start)
	    output = sh (
	        script: "sed '${start},${end}!d' CHANGELOG.md",
	        returnStdout: true
	    ).trim()
	    return output.replace("\"", "").replace("'", "").replace("\\", "").replace	("\n", "\\n")
	}

	private int getChangelogStartIndex(String releaseVersion){
	    startLineString = "## \\[${releaseVersion}\\]"
	    script = "grep -n \"${startLineString}\" CHANGELOG.md | head -1 | sed s/\"^\\	([0-9]*\\)[:].*\$\"/\"\\1\"/g"
	    output = sh (
	        script: script,
	        returnStdout: true
	    ).trim()
	    return (output as int) + 1
	}

	private String getChangelogEndIndex(int start){
	    script = "tail -n +${start+1} CHANGELOG.md |grep -n \"^## \\[.*\\]\" | sed s/	\"^\\([0-9]*\\)[:].*\$\"/\"\\1\"/g | head -1"
	    output = sh (
	        script: script,
	        returnStdout: true
	    ).trim()
	    if ((output as String).length() > 0){
	        return ((output as int) + start - 1) as String
	    }
	    return "\$"
	}

}