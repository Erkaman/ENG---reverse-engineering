
puts 'hello world'
#strings.exe -n 10 D:\Setup/eng/cd/wads/fesound.wad


def mypath(file)
	return "C:/Users/eric/Dropbox/emp/" + file
end

for file in Dir.entries("D:\Setup/eng/cd/wads")
	puts file
	
	out_file = file + ".txt"
	puts out_file
	
	cmd = mypath("strings.exe") + 
" -n 3 " + "D:\Setup/eng/cd/wads/" + file + " > " + 
mypath(out_file) 
	
	system(
     cmd      )
	#puts cmd
end


puts mypath("eric")

#system("strings.exe -n 3 D:\Setup/eng/cd/wads/fesound.wad")

#system("strings.exe")

system(
mypath("strings.exe") + 
" -n 3 D:\Setup/eng/cd/wads/fesound.wad >" + 
mypath("out.txt")   )

#system()