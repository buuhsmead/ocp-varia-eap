#!/usr/bin/env bash

## oc describe template eap71-basic-s2i  -n openshift


## This is an EAR so ARTIFACT_DIR has to be set !
oc new-app --template=eap71-basic-s2i --param-file=create-new-app.params.env --param-file=database.params.env


## oc logs -f bc/hello-ocp

