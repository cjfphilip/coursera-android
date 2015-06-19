
namespace Chronological
{
	public class MatchingLineToMp3
	{
		string line;
		string mp3;
		string result;

		public MatchingLineToMp3(string line, string mp3, string result)
		{
			this.line = line;
			this.mp3 = mp3;
			this.result = result;
		}

		public override string ToString()
		{
			return string.Format("{0} - {1} => {2}", result, line, mp3);
		}
	}
}
