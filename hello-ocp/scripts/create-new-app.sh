#!/usr/bin/env bash

## oc describe template eap71-basic-s2i  -n openshift


## This is an EAR so ARTIFACT_DIR has to be set !
oc new-app --template=eap71-basic-s2i \
 -p APPLICATION_NAME=hello-ocp \
 -p SOURCE_REPOSITORY_URL="https://github.com/buuhsmead/ocp-varia-eap" \
 -p SOURCE_REPOSITORY_REF="master" \
 -p CONTEXT_DIR="hello-ocp" \
 -p ARTIFACT_DIR="hello-ocp-ear/target" \
 -p SCRIPT_DEBUG="true"


## oc logs -f bc/hello-ocp

