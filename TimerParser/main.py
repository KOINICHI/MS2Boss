import codecs

def normalbosses():
	BOSSES = ['giant_turtle', 'mark_alpha']

	def zero(s):
		s = s.split()
		h = ('','0')[(len(s[0])<2)] + s[0]
		m = ('','0')[(len(s[1])<2)] + s[1]
		return h+m

	for boss in BOSSES:
		with codecs.open(boss + '_in.txt', 'r', encoding='utf-8') as file:
			lines = file.read().split('\n')
			AM = lines[0].split(',')
			PM = lines[1].split(',')
			
			f = open(boss + '.txt', 'w');

			f.write("<string-array name=\"" + boss +"\">\n")
			for t in AM:
				time = int(zero(t.encode('ascii', 'ignore').decode('utf-8')))%12
				print (time)
				f.write("\t<item>" + time + "</item>\n")
			print()
			f.write('\n');
			for t in PM:
				time = int(zero(t.encode('ascii', 'ignore').decode('utf-8')))+12
				if time > 23:
					time -= 12
				print (time)
				f.write("\t<item>" + time + "</item>\n")
			f.write("</string-array>\n")
			f.close()
		
def elitebosses():
	def zero(s):
		if len(s) < 2:
			return '0' + s
		return s
	f = open('elite_input.txt', 'r')
	lines = f.read().split('\n')
	f.close()

	n = len(lines)
	idx = 0


	f = open('elite.txt', 'w')
	while idx < n:
		print(lines[idx].split())
		name,m = lines[idx].split()
		idx += 1
		
		f.write("<string-array name=\"" + name +"\">\n")
		for i in range(int(m)): 
			times = lines[idx].split()
			hr_from = times[0]
			hr_end = times[1]
			mins = times[2:]
			for hr in range(int(hr_from), int(hr_end) + 1):
				for min in mins:
					f.write("\t<item>" + zero(str(hr)) + zero(min) + "</item>\n")			
			idx += 1
		f.write("</string-array>\n\n")
	f.close()
	
	
def namenloc():
	f = open('bossinfo.txt', 'r')
	lines = f.read()
	f.close()

	f = open('namenloc.txt', 'w')
	for line in lines.split('\n'):
		filename,name,level,location,type = line.split(',')
		f.write("<string name=\"" + filename + "\">" + name + "</string>\n")
		f.write("<string name=\"" + filename + "_loc\">" + location + "</string>\n")
	f.close()

	f = open('bossdatainit.txt', 'w')
	for line in lines.split('\n'):
		filename,name,level,location,type = line.split(',')
		print (filename, name, level, location, type)
		f.write("bosses.add(new Boss(R.string." + filename +
				", 23, R.string." + filename + "_loc"
				", R.drawable." + filename +
				", R.array." + filename +
				", \"" + type +
				"\", c));\n")
	f.close()

	
def boss_id():
	ids = []
	f = open('bossdatainit.txt','r')
	for line in f.read().split('\n'):
		ret = ""
		for c in line[29:]:
			if c == ',':
				break
			ret += c
		print ("<item>" + ret + "</item>")
	f.close()
	

	

ids = []
f = open('bossinfo.txt','r')
lines = f.read()
f.close()
f = open('bossdetaildata.txt', 'w')
m = {"Elite":"0", "Field Boss":"1", "Raid Boss":"2", "Unknown":"3"}
for line in lines.split('\n'):
	filename,name,level,location,type = line.split(',')
	print(filename)
	f.write("<string name=\"" + filename + "_boss_type\">" + m[type] + "</string>\n")
	f.write("<string name=\"" + filename + "_level\">" + str(level) + "</string>\n")
f.close()