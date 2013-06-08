function save_data(name)
[i,j] = size(name);
fid=fopen('T2/t22.data','a+');
for i0 = 1:i

       fprintf(fid, '0 %d %d %d\n', name(i0, 1),name(i0,2),name(i0,3));
    
end
fclose(fid);
end