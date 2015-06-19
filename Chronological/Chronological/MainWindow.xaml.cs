using System;
using System.Collections.Generic;
using System.IO;
using System.Windows;
using System.Windows.Documents;

using Path = System.IO.Path;

namespace Chronological
{
	/// <summary>
	/// Interaction logic for MainWindow.xaml
	/// </summary>
	public partial class MainWindow : Window
	{
		const string path = @"C:\Users\Charles\Documents\Android\Source\coursera-android\Chronological\Chrono";
		List<MatchingLineToMp3> results = new List<MatchingLineToMp3>();

		public MainWindow()
		{
			InitializeComponent();
			Refresh();
		}

		void Refresh()
		{
			try
			{
				List<String> listOfItems = new List<string>();
				string fileToParse = Path.Combine(path, "order.txt");
				using (StreamReader sr = new StreamReader(fileToParse))
				{
					while (sr.Peek() >= 0)
					{
						String line = sr.ReadLine();
						if (!string.IsNullOrWhiteSpace(line))
						{
							string message;
							List<string> mp3S = FindMatchingMp3S(line, out message);
							results.Add(new MatchingLineToMp3(line, mp3S, message));
						}
					}
				}
				ResultsListBox.ItemsSource = results;
			}
			catch (Exception e)
			{
				Console.WriteLine(e.Message);
			}
		}

		List<string> FindMatchingMp3S(string line, out string message)
		{
			message = "no chapters found";
			List<string> result = new List<string>();
			int splitPos = line.IndexOf(" ");
			if (splitPos > -1)
			{
				try
				{
					string bookToFind = line.Substring(0, splitPos);
					string chapterNumbers = line.Substring(splitPos);
					int hyphenPos = chapterNumbers.IndexOf("-");
					int chapterToStartWith = int.Parse(hyphenPos > -1 ? chapterNumbers.Substring(0, hyphenPos) : chapterNumbers);
					int chapterToEndWith = hyphenPos > -1 ? int.Parse(chapterNumbers.Substring(hyphenPos + 1)) : chapterToStartWith;

					string[] files = Directory.GetFiles(Path.Combine(path, "books"), "*.mp3", SearchOption.AllDirectories);
					foreach (string file in files)
					{
						string fileName = Path.GetFileName(file);

						if (fileName != null && fileName.Substring(3).StartsWith(bookToFind))
						{
							for (int chapter = chapterToStartWith; chapter <= chapterToEndWith; chapter++)
							{
								string chapterToLookFor = String.Format("Chapter {0:D2}.mp3", chapter);
								if (fileName.Contains(chapterToLookFor))
								{
									result.Add(fileName);
									break;
								}
							}
						}
					}

					int totalNeeded = chapterToEndWith - chapterToStartWith + 1;
					message = result.Count == totalNeeded ? "success" : String.Format("{0} mp3s found, but there needs to be {1}", result.Count, totalNeeded);
				}
				catch (Exception ex)
				{
					message = ex.Message;
				}
			}
			return result;
		}

		private void Button_Click(object sender, RoutedEventArgs e)
		{
			Refresh();
		}
	}
}
