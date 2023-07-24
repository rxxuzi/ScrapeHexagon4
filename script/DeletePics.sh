#!/bin/bash

dir="./../output/pics"

if [ -d "$dir" ]; then
  echo "Deleting all files in $dir"
  rm -rf "$dir"/*
else
  echo "$dir does not exist"
fi


dir="./../output/scala"

if [ -d "$dir" ]; then
  echo "Deleting all files in $dir"
  rm -rf "$dir"/*
else
  echo "$dir does not exist"
fi

dir="./../output/json"

if [ -d "$dir" ]; then
  echo "Deleting all files in $dir"
  # shellcheck disable=SC2115
  rm -rf "$dir"/*
else
  echo "$dir does not exist"
fi