
using System.Collections.Generic;

namespace Chronological
{
	public class MatchingLineToMp3
	{
		string line;
		List<string> mp3S;
		string result;

		public MatchingLineToMp3(string line, List<string> mp3S, string result)
		{
			this.line = line;
			this.mp3S = mp3S;
			this.result = result;
		}

		public override string ToString()
		{
			return string.Format("{0} - {1} => {2} mp3s", result, line, mp3S.Count);
		}
	}
}
