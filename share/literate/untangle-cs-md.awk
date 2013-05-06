
# AWK patterns and actions for
# Literate CoffeeScript untangle
BEGIN {
  RS="\n\n"
  FS="\n"
  regexMdHeader = "^\\#{1,6}+"
  regexCsIndent = "^[[:space:]]{4,} | ^[\t]"
  }
  {
    untangle="cs"
    if (NR!=1) {
      if (untangle == "cs" && ( $0 ~ regexCsIndent )) { print }
      if (untangle == "md" && (! ($0 ~ regexCsIndent ))) {
        print "\n";print; if ($0 ~ regexMdHeader) { print "\n" }
      }
    }
  }

#if (! ($0 ~ /^\#{1,6}.*$/ )) { print "\n" $0}1
#if ( untangle == "md" && (! ($0 ~ regexCsIndent ))) { if ($0 ~ regexMdHeader )print "\n\n";1;print $0 }
#if (untangle == "cs") 
#regexCsIndent { print }

