using System;
using System.Collections.Generic;
using System.IO;
using System.Windows;
using System.Windows.Documents;
using System.Linq;

using Path = System.IO.Path;

namespace Chronological
{
	/// <summary>
	/// Interaction logic for MainWindow.xaml
	/// </summary>
	public partial class MainWindow : Window
	{
        const string path = @"C:\Users\C3P4J\Documents\Android\Source\coursera-android\Chronological\Chrono";
		List<MatchingLineToMp3> results = new List<MatchingLineToMp3>();
        List<string> files = new List<string>();
        int totalMp3s;

		public MainWindow()
		{
			InitializeComponent();
            ResultsListBox.ItemsSource = results;
            FileSystemWatcher fileSystemWatcher = new FileSystemWatcher(path);
            fileSystemWatcher.Changed += fileSystemWatcher_Changed;
			Refresh();
            //fileSystemWatcher.EnableRaisingEvents = true;
		}

        void fileSystemWatcher_Changed(object sender, FileSystemEventArgs e)
        {
            Refresh();
        }

        private Object refreshing = new Object();
		void Refresh()
		{
            lock (refreshing)
            {
                results = new List<MatchingLineToMp3>();
                try
                {
                    files = Directory.GetFiles(Path.Combine(path, "books"), "*.mp3", SearchOption.AllDirectories).ToList();
                    totalMp3s = files.Count;
                    List<String> listOfItems = new List<string>();
                    string fileToParse = Path.Combine(path, "order.txt");
                    using (StreamReader sr = new StreamReader(fileToParse))
                    {
                        while (sr.Peek() >= 0)
                        {
                            String line = sr.ReadLine().Trim();
                            if (!string.IsNullOrWhiteSpace(line))
                            {
                                FindMatchingMp3S(line);
                            }
                        }
                    }

                    foreach (string file in files)
                    {
                        results.Add(new MatchingLineToMp3(file, "[unknown]", 0, 0, new List<string>(), "not found in playlist"));
                    }

                    ResultsListBox.ItemsSource = results;
                    int totalFound = results.SelectMany(l => l.mp3S).Count();
                    btnRefresh.Content = String.Format("Copy Files - Found {0} out of {1}", totalFound, totalMp3s);
                }
                catch (Exception e)
                {
                    Console.WriteLine(e.Message);
                }
            }
		}

		void FindMatchingMp3S(string line)
		{
            string message = "no chapters found";
			List<string> result = new List<string>();
			int splitPos = line.LastIndexOf(" ");
			if (splitPos > -1)
			{
				try
				{
					string bookToFind = line.Substring(0, splitPos);
					string chapterNumbers = line.Substring(splitPos+1);
					int hyphenPos = chapterNumbers.IndexOf("-");
					int chapterToStartWith = int.Parse(hyphenPos > -1 ? chapterNumbers.Substring(0, hyphenPos) : chapterNumbers);
					int chapterToEndWith = hyphenPos > -1 ? int.Parse(chapterNumbers.Substring(hyphenPos + 1)) : chapterToStartWith;
                    int totalNeeded = chapterToEndWith - chapterToStartWith + 1;
                    int start = chapterToStartWith;

					for (int i = 0; i < files.Count; i++)
			        {
			            var file = files[i];
						string fileName = Path.GetFileName(file);

						if (fileName != null && fileName.Substring(3).StartsWith(bookToFind))
						{
                            if (chapterToStartWith <= chapterToEndWith)
                            {
                                for (int chapter = chapterToStartWith; chapter <= chapterToEndWith; chapter++)
                                {
                                    string chapterToLookFor = String.Format("Chapter {0:D2}.mp3", chapter);
                                    if (fileName.Contains(chapterToLookFor))
                                    {
                                        result.Add(file);
                                        files.Remove(file);
                                        i--;
                                        chapterToStartWith += 1;
                                        break;
                                    }
                                }
                            }
                            
                            if (chapterToStartWith > chapterToEndWith)
                            {
                                break;
                            }
						}
					}

                    if (totalNeeded > 0)
                    {
                        message = result.Count == totalNeeded ? "success" : String.Format("{0} mp3s found, but there needs to be {1}", result.Count, totalNeeded);
                        results.Add(new MatchingLineToMp3(line, bookToFind, start, chapterToEndWith, result, message));
                    }
                    else
                    {
                        message = "no chapters found";
                        results.Add(new MatchingLineToMp3(line, bookToFind, chapterToStartWith, chapterToEndWith, result, message));
                    }
				}
				catch (Exception ex)
				{
					message = ex.Message;
                    results.Add(new MatchingLineToMp3(line, "???", 0, 0, result, message));
				}
                
			}
            else
            {
                results.Add(new MatchingLineToMp3(line, "no book can be found", 0, 0, result, message));

            }
		}

        void CopyMp3s()
        {
            int i = 1;
            foreach(MatchingLineToMp3 result in results)
            {
                string chapters = (result.start == result.end) ? result.start.ToString() : (result.start + "-" + result.end);
                string destinationPath = Path.Combine(path, "Chronological", i.ToString().PadLeft(3, '0') + " " + result.chapter + " " + chapters);
                Directory.CreateDirectory(destinationPath);
                foreach (string mp3 in result.mp3S)
                {
                    File.Copy(mp3, Path.Combine(destinationPath, Path.GetFileName(mp3)), true);
                }
                i++;
            }
        }

		private void Button_Click(object sender, RoutedEventArgs e)
		{
            CopyMp3s();
		}
	}
}
