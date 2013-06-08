function preprocess(infile,outfile)
%PREPROCESS2 Summary of this function goes here
%   Detailed explanation goes here
% This function is to preprocess the image data
in=load(infile);

%rand_sequence=randperm(size(in,1));
%temp_dataset=in;
%in=temp_dataset(rand_sequence, :);
for i=2:size(in,2)
    in(:,i)=(in(:,i)-min(in(:,i)))/(max(in(:,i))-min(in(:,i)))*2-1;
end

out=fopen(outfile,'w');
for i=1:size(in,1)
        fprintf(out,'%2.8f ', in(i,1));
        fprintf(out,'%2.8f ', in(i,2));
        fprintf(out,'%2.8f ', in(i,3));
        fprintf(out,'%2.8f', in(i,4));
        fprintf(out,'\n');
end
fclose(out);


end

