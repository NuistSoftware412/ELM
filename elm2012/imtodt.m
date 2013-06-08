function imtodt( image_file,data_file )
%IMTODT Summary of this function goes here
%   Detailed explanation goes here

input=imread(image_file);
x=size(input,1);
y=size(input,2);
fid=fopen(data_file,'w');
for i=1:x
    for j=1:y
        fprintf(fid,'%d %d %d\n',input(i,j,1),input(i,j,2),input(i,j,3));
    end
end
fclose(fid);
end

