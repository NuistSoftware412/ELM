input=imread('after2.jpg');
x=size(input,1);
y=size(input,2);
z=size(input,3);
s=x*y;
fid=fopen('after2','w');
for i=1:x
    for j=1:y
        fprintf(fid,'%d %d %d\n',input(i,j,1),input(i,j,2),input(i,j,3));
    end
end
fclose(fid);