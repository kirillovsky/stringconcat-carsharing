function imageTagOrLatest() {
  if [ -z "$1" ]
    then
      local LOCAL="latest"
      echo $LOCAL
      echo "No image tag provided. latest will be used" >&2
  else
    echo "$1"
  fi
}