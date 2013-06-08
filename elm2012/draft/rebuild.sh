#! /bin/sh
latex paper.tex
bibtex paper.aux
latex paper.tex
latex paper.tex
dvipdf paper.dvi
