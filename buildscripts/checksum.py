#!/usr/bin/env python3
"""
Generates updated sha256 checksums from CMakeLists.txt and git diff; very hacky but does its job
"""

import subprocess
import re
import sys
import urllib.request
import hashlib


def eprint(*args, **kwargs):
    print(*args, file=sys.stderr, **kwargs)


class Project:
    def __init__(self, name, url, hash_variable):
        self.name = name
        self.url = url
        self.hash_variable = hash_variable
        self.version_variables = set()
        self.changed = False

    def resolve_url(self, variables):
        url = self.url
        for name, value in variables.items():
            if name in url:
                self.version_variables.add(name)
                url = url.replace("${%s}" % name, value)
        if "$(" in url:
            raise RuntimeError("failed to resolve all variables for URL {}".format(self.url))
        return url


def main():
    with open("CMakeLists.txt", "r") as inf:
        data = inf.read()
    lines = data.split("\n")

    # get the header lines from the file, the header defines versions and hashes
    header = []
    for line in lines:
        header.append(line)
        if "End of configurable options" in line:
            break;
    header = header[2:]

    # resolve all cmake external projects, i.e. calls to ExternalProject_Add
    projects = []
    variables = dict()
    for line in lines:
        if line.startswith("set(") and "_VERSION " in line:
            line = line[4:-1]
            name, value = line.split()
            variables[name] = value

        if line.startswith("ExternalProject_Add"):
            name = line[line.find("(")+1:]
        elif "URL_HASH" in line:
            hash_variable = line.split()[1][2:-1]
        elif "URL" in line:
            url = line.split()[1]

        # insert this project now
        if "DOWNLOAD_DIR" in line:
            projects.append(Project(name, url, hash_variable))

    variables["BOOST_VERSION_UNDERSCORE"] = variables["BOOST_VERSION"].replace(".", "_")

    # resolve the urls in advance to get version_variables
    for project in projects:
        project.resolve_url(variables)

    # figure which projects have changed using git diff
    output = subprocess.check_output(["git", "diff", "--unified=0", "CMakeLists.txt"]).decode("utf-8").split("\n")
    for line in output:
        if line.startswith("-set("):
            for project in projects:
                for var in project.version_variables:
                    if var in line:
                        project.changed = True

    # find out which URLs we need to download and which hash variables to update, the mapping is url->hash variable
    url_to_hash = dict()
    for project in projects:
        if project.changed:
            url_to_hash[project.resolve_url(variables)] = project.hash_variable

    # download the URLs and make note of the new hashes
    new_variables = dict()
    for url, hash_variable in url_to_hash.items():
        eprint("Downloading {}".format(url))

        resp = urllib.request.urlopen(url)
        binary = resp.read()
        h = hashlib.sha256(binary).hexdigest()

        eprint(" => {}".format(h))

        new_hash = "SHA256={}".format(h)
        new_variables[hash_variable] = new_hash

    # insert new variables into the header
    for x in range(len(header)):
        line = header[x]
        for variable, value in new_variables.items():
            if variable in line:
                line = re.sub(r"SHA256=.{64}", value, line)
        header[x] = line
    
    eprint("")
    print("\n".join(header))


if __name__ == "__main__":
    main()
