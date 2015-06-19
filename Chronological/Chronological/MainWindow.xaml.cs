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
		const string path = @"C:\Users\Charles\Desktop\Chrono";
		List<MatchingLineToMp3> results = new List<MatchingLineToMp3>();

		public MainWindow()
		{
			InitializeComponent();
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
							string result = "";
							string mp3 = FindMatchingMp3(line);
							result = (mp3 != null) ? "success" : "count not find mp3";
							results.Add(new MatchingLineToMp3(line, mp3, result));
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

		string FindMatchingMp3(string line)
		{
			string result = "";
			int splitPos = line.IndexOf(" ");
			if (splitPos > -1)
			{
				string bookToFind = line.Substring(0, splitPos);
				string chapterNumbers = line.Substring(splitPos);
				int hyphenPos = chapterNumbers.IndexOf("-");
				int chapterToStartWith = int.Parse(hyphenPos > -1 ? chapterNumbers.Substring(0, hyphenPos) : chapterNumbers);
				int chapterToEndWith = (hyphenPos > -1) ;

				string [] files = Directory.GetFiles(Path.Combine(path, "books"), "*.mp3", SearchOption.AllDirectories);
				foreach (string file in files)
				{
					string mp3 
					if (Path.GetFileName(file).Substring(2).StartsWith(line))
					
				}
				
			}
			return result;
		}
	}
}
