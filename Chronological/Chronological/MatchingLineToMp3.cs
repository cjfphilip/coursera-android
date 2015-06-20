
using System.Collections.Generic;

namespace Chronological
{
	public class MatchingLineToMp3
	{
		public string line;
        public string chapter;
        public int start;
        public int end;
		public List<string> mp3S;
		public string result;

		public MatchingLineToMp3(string line, string chapter, int start, int end, List<string> mp3S, string result)
		{
			this.line = line;
            this.chapter = chapter;
            this.start = start;
            this.end = end;
			this.mp3S = mp3S;
			this.result = result;
		}

		public override string ToString()
		{
			return string.Format("{0} - {1} => {2} mp3s", result, line, (mp3S != null) ? mp3S.Count : 0);
		}
	}
}
