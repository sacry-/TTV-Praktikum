#!/bin/bash

set -e -o pipefail


texBin="/Library/TeX/texbin"

function clear_tex {
  rm -f $1.log
  rm -f $1.aux
}

function biblio {
  $texBin/bibtex $1.aux
}

function texify {
  $texBin/pdflatex -interaction=batchmode $1.tex
}

function run {
  folder=$1
  fname=$2

  pushd $folder

  clear_tex $fname
  texify $fname
  clear_tex $fname
  open $fname.pdf

  popd
}


folder="./tex"
if [[ -n "$1" ]]; then
  folder="$1"
fi

fname="ttv-protokoll-1"
if [[ -n "$2" ]]; then
  fname="$2"
fi

run $folder $fname

